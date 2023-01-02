import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMovie, NewMovie } from '../movie.model';

export type PartialUpdateMovie = Partial<IMovie> & Pick<IMovie, 'id'>;

export type EntityResponseType = HttpResponse<IMovie>;
export type EntityArrayResponseType = HttpResponse<IMovie[]>;

@Injectable({ providedIn: 'root' })
export class MovieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/movies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(movie: NewMovie): Observable<EntityResponseType> {
    return this.http.post<IMovie>(this.resourceUrl, movie, { observe: 'response' });
  }

  update(movie: IMovie): Observable<EntityResponseType> {
    return this.http.put<IMovie>(`${this.resourceUrl}/${this.getMovieIdentifier(movie)}`, movie, { observe: 'response' });
  }

  partialUpdate(movie: PartialUpdateMovie): Observable<EntityResponseType> {
    return this.http.patch<IMovie>(`${this.resourceUrl}/${this.getMovieIdentifier(movie)}`, movie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMovie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMovie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMovieIdentifier(movie: Pick<IMovie, 'id'>): number {
    return movie.id;
  }

  compareMovie(o1: Pick<IMovie, 'id'> | null, o2: Pick<IMovie, 'id'> | null): boolean {
    return o1 && o2 ? this.getMovieIdentifier(o1) === this.getMovieIdentifier(o2) : o1 === o2;
  }

  addMovieToCollectionIfMissing<Type extends Pick<IMovie, 'id'>>(
    movieCollection: Type[],
    ...moviesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const movies: Type[] = moviesToCheck.filter(isPresent);
    if (movies.length > 0) {
      const movieCollectionIdentifiers = movieCollection.map(movieItem => this.getMovieIdentifier(movieItem)!);
      const moviesToAdd = movies.filter(movieItem => {
        const movieIdentifier = this.getMovieIdentifier(movieItem);
        if (movieCollectionIdentifiers.includes(movieIdentifier)) {
          return false;
        }
        movieCollectionIdentifiers.push(movieIdentifier);
        return true;
      });
      return [...moviesToAdd, ...movieCollection];
    }
    return movieCollection;
  }
}
