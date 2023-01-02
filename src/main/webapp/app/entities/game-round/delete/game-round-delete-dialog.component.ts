import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGameRound } from '../game-round.model';
import { GameRoundService } from '../service/game-round.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './game-round-delete-dialog.component.html',
})
export class GameRoundDeleteDialogComponent {
  gameRound?: IGameRound;

  constructor(protected gameRoundService: GameRoundService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameRoundService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
