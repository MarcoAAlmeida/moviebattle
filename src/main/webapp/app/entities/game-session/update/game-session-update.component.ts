import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GameSessionFormService, GameSessionFormGroup } from './game-session-form.service';
import { IGameSession } from '../game-session.model';
import { GameSessionService } from '../service/game-session.service';

@Component({
  selector: 'jhi-game-session-update',
  templateUrl: './game-session-update.component.html',
})
export class GameSessionUpdateComponent implements OnInit {
  isSaving = false;
  gameSession: IGameSession | null = null;

  editForm: GameSessionFormGroup = this.gameSessionFormService.createGameSessionFormGroup();

  constructor(
    protected gameSessionService: GameSessionService,
    protected gameSessionFormService: GameSessionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameSession }) => {
      this.gameSession = gameSession;
      if (gameSession) {
        this.updateForm(gameSession);
      }
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
  }
}
