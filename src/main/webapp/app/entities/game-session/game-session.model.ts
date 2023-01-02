import { IGameRound } from 'app/entities/game-round/game-round.model';

export interface IGameSession {
  id: number;
  userId?: number | null;
  gameRound?: Pick<IGameRound, 'id'> | null;
}

export type NewGameSession = Omit<IGameSession, 'id'> & { id: null };
