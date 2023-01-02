import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GameRoundFormService, GameRoundFormGroup } from './game-round-form.service';
import { IGameRound } from '../game-round.model';
import { GameRoundService } from '../service/game-round.service';
import { IGameSession } from 'app/entities/game-session/game-session.model';
import { GameSessionService } from 'app/entities/game-session/service/game-session.service';
import { Choice } from 'app/entities/enumerations/choice.model';

@Component({
  selector: 'jhi-game-round-update',
  templateUrl: './game-round-update.component.html',
})
export class GameRoundUpdateComponent implements OnInit {
  isSaving = false;
  gameRound: IGameRound | null = null;
  choiceValues = Object.keys(Choice);

  gameSessionsSharedCollection: IGameSession[] = [];

  editForm: GameRoundFormGroup = this.gameRoundFormService.createGameRoundFormGroup();

  constructor(
    protected gameRoundService: GameRoundService,
    protected gameRoundFormService: GameRoundFormService,
    protected gameSessionService: GameSessionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGameSession = (o1: IGameSession | null, o2: IGameSession | null): boolean => this.gameSessionService.compareGameSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameRound }) => {
      this.gameRound = gameRound;
      if (gameRound) {
        this.updateForm(gameRound);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gameRound = this.gameRoundFormService.getGameRound(this.editForm);
    if (gameRound.id !== null) {
      this.subscribeToSaveResponse(this.gameRoundService.update(gameRound));
    } else {
      this.subscribeToSaveResponse(this.gameRoundService.create(gameRound));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameRound>>): void {
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

  protected updateForm(gameRound: IGameRound): void {
    this.gameRound = gameRound;
    this.gameRoundFormService.resetForm(this.editForm, gameRound);

    this.gameSessionsSharedCollection = this.gameSessionService.addGameSessionToCollectionIfMissing<IGameSession>(
      this.gameSessionsSharedCollection,
      gameRound.gameSessionId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameSessionService
      .query()
      .pipe(map((res: HttpResponse<IGameSession[]>) => res.body ?? []))
      .pipe(
        map((gameSessions: IGameSession[]) =>
          this.gameSessionService.addGameSessionToCollectionIfMissing<IGameSession>(gameSessions, this.gameRound?.gameSessionId)
        )
      )
      .subscribe((gameSessions: IGameSession[]) => (this.gameSessionsSharedCollection = gameSessions));
  }
}
