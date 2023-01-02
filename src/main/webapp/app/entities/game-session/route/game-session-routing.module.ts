import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GameSessionComponent } from '../list/game-session.component';
import { GameSessionDetailComponent } from '../detail/game-session-detail.component';
import { GameSessionUpdateComponent } from '../update/game-session-update.component';
import { GameSessionRoutingResolveService } from './game-session-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gameSessionRoute: Routes = [
  {
    path: '',
    component: GameSessionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameSessionDetailComponent,
    resolve: {
      gameSession: GameSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameSessionUpdateComponent,
    resolve: {
      gameSession: GameSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameSessionUpdateComponent,
    resolve: {
      gameSession: GameSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gameSessionRoute)],
  exports: [RouterModule],
})
export class GameSessionRoutingModule {}
