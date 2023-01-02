import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../game-round.test-samples';

import { GameRoundFormService } from './game-round-form.service';

describe('GameRound Form Service', () => {
  let service: GameRoundFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameRoundFormService);
  });

  describe('Service methods', () => {
    describe('createGameRoundFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGameRoundFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            gameSessionId: expect.any(Object),
            rightImdbId: expect.any(Object),
            leftImdbId: expect.any(Object),
            userChoice: expect.any(Object),
            correct: expect.any(Object),
          })
        );
      });

      it('passing IGameRound should create a new form with FormGroup', () => {
        const formGroup = service.createGameRoundFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            gameSessionId: expect.any(Object),
            rightImdbId: expect.any(Object),
            leftImdbId: expect.any(Object),
            userChoice: expect.any(Object),
            correct: expect.any(Object),
          })
        );
      });
    });

    describe('getGameRound', () => {
      it('should return NewGameRound for default GameRound initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGameRoundFormGroup(sampleWithNewData);

        const gameRound = service.getGameRound(formGroup) as any;

        expect(gameRound).toMatchObject(sampleWithNewData);
      });

      it('should return NewGameRound for empty GameRound initial value', () => {
        const formGroup = service.createGameRoundFormGroup();

        const gameRound = service.getGameRound(formGroup) as any;

        expect(gameRound).toMatchObject({});
      });

      it('should return IGameRound', () => {
        const formGroup = service.createGameRoundFormGroup(sampleWithRequiredData);

        const gameRound = service.getGameRound(formGroup) as any;

        expect(gameRound).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGameRound should not enable id FormControl', () => {
        const formGroup = service.createGameRoundFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGameRound should disable id FormControl', () => {
        const formGroup = service.createGameRoundFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
