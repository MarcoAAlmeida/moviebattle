import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MovieFormService } from './movie-form.service';
import { MovieService } from '../service/movie.service';
import { IMovie } from '../movie.model';
import { IGameRound } from 'app/entities/game-round/game-round.model';
import { GameRoundService } from 'app/entities/game-round/service/game-round.service';

import { MovieUpdateComponent } from './movie-update.component';

describe('Movie Management Update Component', () => {
  let comp: MovieUpdateComponent;
  let fixture: ComponentFixture<MovieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let movieFormService: MovieFormService;
  let movieService: MovieService;
  let gameRoundService: GameRoundService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MovieUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MovieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MovieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    movieFormService = TestBed.inject(MovieFormService);
    movieService = TestBed.inject(MovieService);
    gameRoundService = TestBed.inject(GameRoundService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GameRound query and add missing value', () => {
      const movie: IMovie = { id: 456 };
      const rights: IGameRound[] = [{ id: 60781 }];
      movie.rights = rights;
      const lefts: IGameRound[] = [{ id: 61615 }];
      movie.lefts = lefts;

      const gameRoundCollection: IGameRound[] = [{ id: 32181 }];
      jest.spyOn(gameRoundService, 'query').mockReturnValue(of(new HttpResponse({ body: gameRoundCollection })));
      const additionalGameRounds = [...rights, ...lefts];
      const expectedCollection: IGameRound[] = [...additionalGameRounds, ...gameRoundCollection];
      jest.spyOn(gameRoundService, 'addGameRoundToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ movie });
      comp.ngOnInit();

      expect(gameRoundService.query).toHaveBeenCalled();
      expect(gameRoundService.addGameRoundToCollectionIfMissing).toHaveBeenCalledWith(
        gameRoundCollection,
        ...additionalGameRounds.map(expect.objectContaining)
      );
      expect(comp.gameRoundsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const movie: IMovie = { id: 456 };
      const right: IGameRound = { id: 33165 };
      movie.rights = [right];
      const left: IGameRound = { id: 83060 };
      movie.lefts = [left];

      activatedRoute.data = of({ movie });
      comp.ngOnInit();

      expect(comp.gameRoundsSharedCollection).toContain(right);
      expect(comp.gameRoundsSharedCollection).toContain(left);
      expect(comp.movie).toEqual(movie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovie>>();
      const movie = { id: 123 };
      jest.spyOn(movieFormService, 'getMovie').mockReturnValue(movie);
      jest.spyOn(movieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movie }));
      saveSubject.complete();

      // THEN
      expect(movieFormService.getMovie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(movieService.update).toHaveBeenCalledWith(expect.objectContaining(movie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovie>>();
      const movie = { id: 123 };
      jest.spyOn(movieFormService, 'getMovie').mockReturnValue({ id: null });
      jest.spyOn(movieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movie }));
      saveSubject.complete();

      // THEN
      expect(movieFormService.getMovie).toHaveBeenCalled();
      expect(movieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovie>>();
      const movie = { id: 123 };
      jest.spyOn(movieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(movieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGameRound', () => {
      it('Should forward to gameRoundService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gameRoundService, 'compareGameRound');
        comp.compareGameRound(entity, entity2);
        expect(gameRoundService.compareGameRound).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
