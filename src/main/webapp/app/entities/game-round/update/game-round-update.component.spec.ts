import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameRoundFormService } from './game-round-form.service';
import { GameRoundService } from '../service/game-round.service';
import { IGameRound } from '../game-round.model';

import { GameRoundUpdateComponent } from './game-round-update.component';

describe('GameRound Management Update Component', () => {
  let comp: GameRoundUpdateComponent;
  let fixture: ComponentFixture<GameRoundUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameRoundFormService: GameRoundFormService;
  let gameRoundService: GameRoundService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GameRoundUpdateComponent],
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
      .overrideTemplate(GameRoundUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameRoundUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameRoundFormService = TestBed.inject(GameRoundFormService);
    gameRoundService = TestBed.inject(GameRoundService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gameRound: IGameRound = { id: 456 };

      activatedRoute.data = of({ gameRound });
      comp.ngOnInit();

      expect(comp.gameRound).toEqual(gameRound);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameRound>>();
      const gameRound = { id: 123 };
      jest.spyOn(gameRoundFormService, 'getGameRound').mockReturnValue(gameRound);
      jest.spyOn(gameRoundService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameRound });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameRound }));
      saveSubject.complete();

      // THEN
      expect(gameRoundFormService.getGameRound).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameRoundService.update).toHaveBeenCalledWith(expect.objectContaining(gameRound));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameRound>>();
      const gameRound = { id: 123 };
      jest.spyOn(gameRoundFormService, 'getGameRound').mockReturnValue({ id: null });
      jest.spyOn(gameRoundService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameRound: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameRound }));
      saveSubject.complete();

      // THEN
      expect(gameRoundFormService.getGameRound).toHaveBeenCalled();
      expect(gameRoundService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameRound>>();
      const gameRound = { id: 123 };
      jest.spyOn(gameRoundService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameRound });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameRoundService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
