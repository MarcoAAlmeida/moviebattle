import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'game-session',
        data: { pageTitle: 'moviebattleApp.gameSession.home.title' },
        loadChildren: () => import('./game-session/game-session.module').then(m => m.GameSessionModule),
      },
      {
        path: 'game-round',
        data: { pageTitle: 'moviebattleApp.gameRound.home.title' },
        loadChildren: () => import('./game-round/game-round.module').then(m => m.GameRoundModule),
      },
      {
        path: 'movie',
        data: { pageTitle: 'moviebattleApp.movie.home.title' },
        loadChildren: () => import('./movie/movie.module').then(m => m.MovieModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
