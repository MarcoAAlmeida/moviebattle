import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameSession } from '../game-session.model';
import { GameSessionService } from '../service/game-session.service';

@Injectable({ providedIn: 'root' })
export class GameSessionRoutingResolveService implements Resolve<IGameSession | null> {
  constructor(protected service: GameSessionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGameSession | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gameSession: HttpResponse<IGameSession>) => {
          if (gameSession.body) {
            return of(gameSession.body);
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
