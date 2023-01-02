import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMovie } from '../movie.model';
import { MovieService } from '../service/movie.service';

@Injectable({ providedIn: 'root' })
export class MovieRoutingResolveService implements Resolve<IMovie | null> {
  constructor(protected service: MovieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMovie | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((movie: HttpResponse<IMovie>) => {
          if (movie.body) {
            return of(movie.body);
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
