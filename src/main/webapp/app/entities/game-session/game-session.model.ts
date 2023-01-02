import { IGameRound } from 'app/entities/game-round/game-round.model';

export interface IGameSession {
  id: string;
  userId?: number | null;
  id?: Pick<IGameRound, 'id'> | null;
}

export type NewGameSession = Omit<IGameSession, 'id'> & { id: null };
