import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GameSessionFormService, GameSessionFormGroup } from './game-session-form.service';
import { IGameSession } from '../game-session.model';
import { GameSessionService } from '../service/game-session.service';
import { IGameRound } from 'app/entities/game-round/game-round.model';
import { GameRoundService } from 'app/entities/game-round/service/game-round.service';

@Component({
  selector: 'jhi-game-session-update',
  templateUrl: './game-session-update.component.html',
})
export class GameSessionUpdateComponent implements OnInit {
  isSaving = false;
  gameSession: IGameSession | null = null;

  gameRoundsSharedCollection: IGameRound[] = [];

  editForm: GameSessionFormGroup = this.gameSessionFormService.createGameSessionFormGroup();

  constructor(
    protected gameSessionService: GameSessionService,
    protected gameSessionFormService: GameSessionFormService,
    protected gameRoundService: GameRoundService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGameRound = (o1: IGameRound | null, o2: IGameRound | null): boolean => this.gameRoundService.compareGameRound(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameSession }) => {
      this.gameSession = gameSession;
      if (gameSession) {
        this.updateForm(gameSession);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gameSession = this.gameSessionFormService.getGameSession(this.editForm);
    if (gameSession.id !== null) {
      this.subscribeToSaveResponse(this.gameSessionService.update(gameSession));
    } else {
      this.subscribeToSaveResponse(this.gameSessionService.create(gameSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameSession>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gameSession: IGameSession): void {
    this.gameSession = gameSession;
    this.gameSessionFormService.resetForm(this.editForm, gameSession);

    this.gameRoundsSharedCollection = this.gameRoundService.addGameRoundToCollectionIfMissing<IGameRound>(
      this.gameRoundsSharedCollection,
      gameSession.gameRound
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameRoundService
      .query()
      .pipe(map((res: HttpResponse<IGameRound[]>) => res.body ?? []))
      .pipe(
        map((gameRounds: IGameRound[]) =>
          this.gameRoundService.addGameRoundToCollectionIfMissing<IGameRound>(gameRounds, this.gameSession?.gameRound)
        )
      )
      .subscribe((gameRounds: IGameRound[]) => (this.gameRoundsSharedCollection = gameRounds));
  }
}
