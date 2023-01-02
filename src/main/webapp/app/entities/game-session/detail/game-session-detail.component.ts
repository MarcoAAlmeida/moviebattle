import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGameSession } from '../game-session.model';

@Component({
  selector: 'jhi-game-session-detail',
  templateUrl: './game-session-detail.component.html',
})
export class GameSessionDetailComponent implements OnInit {
  gameSession: IGameSession | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameSession }) => {
      this.gameSession = gameSession;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
