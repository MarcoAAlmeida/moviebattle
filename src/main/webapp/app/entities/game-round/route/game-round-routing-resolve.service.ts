import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameRound } from '../game-round.model';
import { GameRoundService } from '../service/game-round.service';

@Injectable({ providedIn: 'root' })
export class GameRoundRoutingResolveService implements Resolve<IGameRound | null> {
  constructor(protected service: GameRoundService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGameRound | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gameRound: HttpResponse<IGameRound>) => {
          if (gameRound.body) {
            return of(gameRound.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
