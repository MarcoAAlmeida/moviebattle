import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGameRound, NewGameRound } from '../game-round.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameRound for edit and NewGameRoundFormGroupInput for create.
 */
type GameRoundFormGroupInput = IGameRound | PartialWithRequiredKeyOf<NewGameRound>;

type GameRoundFormDefaults = Pick<NewGameRound, 'id' | 'correct'>;

type GameRoundFormGroupContent = {
  id: FormControl<IGameRound['id'] | NewGameRound['id']>;
  gameSessionId: FormControl<IGameRound['gameSessionId']>;
  rightImdbId: FormControl<IGameRound['rightImdbId']>;
  leftImdbId: FormControl<IGameRound['leftImdbId']>;
  userChoice: FormControl<IGameRound['userChoice']>;
  correct: FormControl<IGameRound['correct']>;
};

export type GameRoundFormGroup = FormGroup<GameRoundFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameRoundFormService {
  createGameRoundFormGroup(gameRound: GameRoundFormGroupInput = { id: null }): GameRoundFormGroup {
    const gameRoundRawValue = {
      ...this.getFormDefaults(),
      ...gameRound,
    };
    return new FormGroup<GameRoundFormGroupContent>({
      id: new FormControl(
        { value: gameRoundRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      gameSessionId: new FormControl(gameRoundRawValue.gameSessionId),
      rightImdbId: new FormControl(gameRoundRawValue.rightImdbId),
      leftImdbId: new FormControl(gameRoundRawValue.leftImdbId),
      userChoice: new FormControl(gameRoundRawValue.userChoice),
      correct: new FormControl(gameRoundRawValue.correct),
    });
  }

  getGameRound(form: GameRoundFormGroup): IGameRound | NewGameRound {
    return form.getRawValue() as IGameRound | NewGameRound;
  }

  resetForm(form: GameRoundFormGroup, gameRound: GameRoundFormGroupInput): void {
    const gameRoundRawValue = { ...this.getFormDefaults(), ...gameRound };
    form.reset(
      {
        ...gameRoundRawValue,
        id: { value: gameRoundRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameRoundFormDefaults {
    return {
      id: null,
      correct: false,
    };
  }
}
