import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameRound, NewGameRound } from '../game-round.model';

export type PartialUpdateGameRound = Partial<IGameRound> & Pick<IGameRound, 'id'>;

export type EntityResponseType = HttpResponse<IGameRound>;
export type EntityArrayResponseType = HttpResponse<IGameRound[]>;

@Injectable({ providedIn: 'root' })
export class GameRoundService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-rounds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gameRound: NewGameRound): Observable<EntityResponseType> {
    return this.http.post<IGameRound>(this.resourceUrl, gameRound, { observe: 'response' });
  }

  update(gameRound: IGameRound): Observable<EntityResponseType> {
    return this.http.put<IGameRound>(`${this.resourceUrl}/${this.getGameRoundIdentifier(gameRound)}`, gameRound, { observe: 'response' });
  }

  partialUpdate(gameRound: PartialUpdateGameRound): Observable<EntityResponseType> {
    return this.http.patch<IGameRound>(`${this.resourceUrl}/${this.getGameRoundIdentifier(gameRound)}`, gameRound, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGameRound>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGameRound[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameRoundIdentifier(gameRound: Pick<IGameRound, 'id'>): number {
    return gameRound.id;
  }

  compareGameRound(o1: Pick<IGameRound, 'id'> | null, o2: Pick<IGameRound, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameRoundIdentifier(o1) === this.getGameRoundIdentifier(o2) : o1 === o2;
  }

  addGameRoundToCollectionIfMissing<Type extends Pick<IGameRound, 'id'>>(
    gameRoundCollection: Type[],
    ...gameRoundsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameRounds: Type[] = gameRoundsToCheck.filter(isPresent);
    if (gameRounds.length > 0) {
      const gameRoundCollectionIdentifiers = gameRoundCollection.map(gameRoundItem => this.getGameRoundIdentifier(gameRoundItem)!);
      const gameRoundsToAdd = gameRounds.filter(gameRoundItem => {
        const gameRoundIdentifier = this.getGameRoundIdentifier(gameRoundItem);
        if (gameRoundCollectionIdentifiers.includes(gameRoundIdentifier)) {
          return false;
        }
        gameRoundCollectionIdentifiers.push(gameRoundIdentifier);
        return true;
      });
      return [...gameRoundsToAdd, ...gameRoundCollection];
    }
    return gameRoundCollection;
  }
}
