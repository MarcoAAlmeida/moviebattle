import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GameRoundComponent } from '../list/game-round.component';
import { GameRoundDetailComponent } from '../detail/game-round-detail.component';
import { GameRoundUpdateComponent } from '../update/game-round-update.component';
import { GameRoundRoutingResolveService } from './game-round-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gameRoundRoute: Routes = [
  {
    path: '',
    component: GameRoundComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameRoundDetailComponent,
    resolve: {
      gameRound: GameRoundRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameRoundUpdateComponent,
    resolve: {
      gameRound: GameRoundRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameRoundUpdateComponent,
    resolve: {
      gameRound: GameRoundRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gameRoundRoute)],
  exports: [RouterModule],
})
export class GameRoundRoutingModule {}
