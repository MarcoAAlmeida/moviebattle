import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGameRound } from '../game-round.model';

@Component({
  selector: 'jhi-game-round-detail',
  templateUrl: './game-round-detail.component.html',
})
export class GameRoundDetailComponent implements OnInit {
  gameRound: IGameRound | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameRound }) => {
      this.gameRound = gameRound;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
