import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameSessionFormService } from './game-session-form.service';
import { GameSessionService } from '../service/game-session.service';
import { IGameSession } from '../game-session.model';
import { IGameRound } from 'app/entities/game-round/game-round.model';
import { GameRoundService } from 'app/entities/game-round/service/game-round.service';

import { GameSessionUpdateComponent } from './game-session-update.component';

describe('GameSession Management Update Component', () => {
  let comp: GameSessionUpdateComponent;
  let fixture: ComponentFixture<GameSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameSessionFormService: GameSessionFormService;
  let gameSessionService: GameSessionService;
  let gameRoundService: GameRoundService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GameSessionUpdateComponent],
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
      .overrideTemplate(GameSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameSessionFormService = TestBed.inject(GameSessionFormService);
    gameSessionService = TestBed.inject(GameSessionService);
    gameRoundService = TestBed.inject(GameRoundService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GameRound query and add missing value', () => {
      const gameSession: IGameSession = { id: 'CBA' };
      const id: IGameRound = { id: 44393 };
      gameSession.id = id;

      const gameRoundCollection: IGameRound[] = [{ id: 51374 }];
      jest.spyOn(gameRoundService, 'query').mockReturnValue(of(new HttpResponse({ body: gameRoundCollection })));
      const additionalGameRounds = [id];
      const expectedCollection: IGameRound[] = [...additionalGameRounds, ...gameRoundCollection];
      jest.spyOn(gameRoundService, 'addGameRoundToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      expect(gameRoundService.query).toHaveBeenCalled();
      expect(gameRoundService.addGameRoundToCollectionIfMissing).toHaveBeenCalledWith(
        gameRoundCollection,
        ...additionalGameRounds.map(expect.objectContaining)
      );
      expect(comp.gameRoundsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameSession: IGameSession = { id: 'CBA' };
      const id: IGameRound = { id: 75334 };
      gameSession.id = id;

      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      expect(comp.gameRoundsSharedCollection).toContain(id);
      expect(comp.gameSession).toEqual(gameSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameSession>>();
      const gameSession = { id: 'ABC' };
      jest.spyOn(gameSessionFormService, 'getGameSession').mockReturnValue(gameSession);
      jest.spyOn(gameSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameSession }));
      saveSubject.complete();

      // THEN
      expect(gameSessionFormService.getGameSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameSessionService.update).toHaveBeenCalledWith(expect.objectContaining(gameSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameSession>>();
      const gameSession = { id: 'ABC' };
      jest.spyOn(gameSessionFormService, 'getGameSession').mockReturnValue({ id: null });
      jest.spyOn(gameSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameSession }));
      saveSubject.complete();

      // THEN
      expect(gameSessionFormService.getGameSession).toHaveBeenCalled();
      expect(gameSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameSession>>();
      const gameSession = { id: 'ABC' };
      jest.spyOn(gameSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameSessionService.update).toHaveBeenCalled();
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
