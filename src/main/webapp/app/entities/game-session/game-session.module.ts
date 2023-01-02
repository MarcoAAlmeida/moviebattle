import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GameSessionComponent } from './list/game-session.component';
import { GameSessionDetailComponent } from './detail/game-session-detail.component';
import { GameSessionUpdateComponent } from './update/game-session-update.component';
import { GameSessionDeleteDialogComponent } from './delete/game-session-delete-dialog.component';
import { GameSessionRoutingModule } from './route/game-session-routing.module';

@NgModule({
  imports: [SharedModule, GameSessionRoutingModule],
  declarations: [GameSessionComponent, GameSessionDetailComponent, GameSessionUpdateComponent, GameSessionDeleteDialogComponent],
})
export class GameSessionModule {}
