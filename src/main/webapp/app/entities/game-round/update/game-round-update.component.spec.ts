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
import { IGameSession } from 'app/entities/game-session/game-session.model';
import { GameSessionService } from 'app/entities/game-session/service/game-session.service';

import { GameRoundUpdateComponent } from './game-round-update.component';

describe('GameRound Management Update Component', () => {
  let comp: GameRoundUpdateComponent;
  let fixture: ComponentFixture<GameRoundUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameRoundFormService: GameRoundFormService;
  let gameRoundService: GameRoundService;
  let gameSessionService: GameSessionService;

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
    gameSessionService = TestBed.inject(GameSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GameSession query and add missing value', () => {
      const gameRound: IGameRound = { id: 456 };
      const gameSessionId: IGameSession = { id: 5790 };
      gameRound.gameSessionId = gameSessionId;

      const gameSessionCollection: IGameSession[] = [{ id: 9933 }];
      jest.spyOn(gameSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: gameSessionCollection })));
      const additionalGameSessions = [gameSessionId];
      const expectedCollection: IGameSession[] = [...additionalGameSessions, ...gameSessionCollection];
      jest.spyOn(gameSessionService, 'addGameSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameRound });
      comp.ngOnInit();

      expect(gameSessionService.query).toHaveBeenCalled();
      expect(gameSessionService.addGameSessionToCollectionIfMissing).toHaveBeenCalledWith(
        gameSessionCollection,
        ...additionalGameSessions.map(expect.objectContaining)
      );
      expect(comp.gameSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameRound: IGameRound = { id: 456 };
      const gameSessionId: IGameSession = { id: 96643 };
      gameRound.gameSessionId = gameSessionId;

      activatedRoute.data = of({ gameRound });
      comp.ngOnInit();

      expect(comp.gameSessionsSharedCollection).toContain(gameSessionId);
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

  describe('Compare relationships', () => {
    describe('compareGameSession', () => {
      it('Should forward to gameSessionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gameSessionService, 'compareGameSession');
        comp.compareGameSession(entity, entity2);
        expect(gameSessionService.compareGameSession).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
