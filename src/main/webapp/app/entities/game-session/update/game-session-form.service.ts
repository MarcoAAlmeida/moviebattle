import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGameSession, NewGameSession } from '../game-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameSession for edit and NewGameSessionFormGroupInput for create.
 */
type GameSessionFormGroupInput = IGameSession | PartialWithRequiredKeyOf<NewGameSession>;

type GameSessionFormDefaults = Pick<NewGameSession, 'id'>;

type GameSessionFormGroupContent = {
  id: FormControl<IGameSession['id'] | NewGameSession['id']>;
  userId: FormControl<IGameSession['userId']>;
};

export type GameSessionFormGroup = FormGroup<GameSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameSessionFormService {
  createGameSessionFormGroup(gameSession: GameSessionFormGroupInput = { id: null }): GameSessionFormGroup {
    const gameSessionRawValue = {
      ...this.getFormDefaults(),
      ...gameSession,
    };
    return new FormGroup<GameSessionFormGroupContent>({
      id: new FormControl(
        { value: gameSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userId: new FormControl(gameSessionRawValue.userId),
    });
  }

  getGameSession(form: GameSessionFormGroup): IGameSession | NewGameSession {
    return form.getRawValue() as IGameSession | NewGameSession;
  }

  resetForm(form: GameSessionFormGroup, gameSession: GameSessionFormGroupInput): void {
    const gameSessionRawValue = { ...this.getFormDefaults(), ...gameSession };
    form.reset(
      {
        ...gameSessionRawValue,
        id: { value: gameSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameSessionFormDefaults {
    return {
      id: null,
    };
  }
}
