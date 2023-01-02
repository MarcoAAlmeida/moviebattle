import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GameRoundComponent } from './list/game-round.component';
import { GameRoundDetailComponent } from './detail/game-round-detail.component';
import { GameRoundUpdateComponent } from './update/game-round-update.component';
import { GameRoundDeleteDialogComponent } from './delete/game-round-delete-dialog.component';
import { GameRoundRoutingModule } from './route/game-round-routing.module';

@NgModule({
  imports: [SharedModule, GameRoundRoutingModule],
  declarations: [GameRoundComponent, GameRoundDetailComponent, GameRoundUpdateComponent, GameRoundDeleteDialogComponent],
})
export class GameRoundModule {}
