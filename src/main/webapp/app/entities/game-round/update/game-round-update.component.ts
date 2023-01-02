import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GameRoundFormService, GameRoundFormGroup } from './game-round-form.service';
import { IGameRound } from '../game-round.model';
import { GameRoundService } from '../service/game-round.service';
import { Choice } from 'app/entities/enumerations/choice.model';

@Component({
  selector: 'jhi-game-round-update',
  templateUrl: './game-round-update.component.html',
})
export class GameRoundUpdateComponent implements OnInit {
  isSaving = false;
  gameRound: IGameRound | null = null;
  choiceValues = Object.keys(Choice);

  editForm: GameRoundFormGroup = this.gameRoundFormService.createGameRoundFormGroup();

  constructor(
    protected gameRoundService: GameRoundService,
    protected gameRoundFormService: GameRoundFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameRound }) => {
      this.gameRound = gameRound;
      if (gameRound) {
        this.updateForm(gameRound);
      }
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
  }
}
