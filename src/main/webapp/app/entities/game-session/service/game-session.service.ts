import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameSession, NewGameSession } from '../game-session.model';

export type PartialUpdateGameSession = Partial<IGameSession> & Pick<IGameSession, 'id'>;

export type EntityResponseType = HttpResponse<IGameSession>;
export type EntityArrayResponseType = HttpResponse<IGameSession[]>;

@Injectable({ providedIn: 'root' })
export class GameSessionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-sessions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gameSession: NewGameSession): Observable<EntityResponseType> {
    return this.http.post<IGameSession>(this.resourceUrl, gameSession, { observe: 'response' });
  }

  update(gameSession: IGameSession): Observable<EntityResponseType> {
    return this.http.put<IGameSession>(`${this.resourceUrl}/${this.getGameSessionIdentifier(gameSession)}`, gameSession, {
      observe: 'response',
    });
  }

  partialUpdate(gameSession: PartialUpdateGameSession): Observable<EntityResponseType> {
    return this.http.patch<IGameSession>(`${this.resourceUrl}/${this.getGameSessionIdentifier(gameSession)}`, gameSession, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGameSession>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGameSession[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameSessionIdentifier(gameSession: Pick<IGameSession, 'id'>): number {
    return gameSession.id;
  }

  compareGameSession(o1: Pick<IGameSession, 'id'> | null, o2: Pick<IGameSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameSessionIdentifier(o1) === this.getGameSessionIdentifier(o2) : o1 === o2;
  }

  addGameSessionToCollectionIfMissing<Type extends Pick<IGameSession, 'id'>>(
    gameSessionCollection: Type[],
    ...gameSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameSessions: Type[] = gameSessionsToCheck.filter(isPresent);
    if (gameSessions.length > 0) {
      const gameSessionCollectionIdentifiers = gameSessionCollection.map(
        gameSessionItem => this.getGameSessionIdentifier(gameSessionItem)!
      );
      const gameSessionsToAdd = gameSessions.filter(gameSessionItem => {
        const gameSessionIdentifier = this.getGameSessionIdentifier(gameSessionItem);
        if (gameSessionCollectionIdentifiers.includes(gameSessionIdentifier)) {
          return false;
        }
        gameSessionCollectionIdentifiers.push(gameSessionIdentifier);
        return true;
      });
      return [...gameSessionsToAdd, ...gameSessionCollection];
    }
    return gameSessionCollection;
  }
}
