import { IGameSession } from 'app/entities/game-session/game-session.model';
import { IMovie } from 'app/entities/movie/movie.model';
import { Choice } from 'app/entities/enumerations/choice.model';

export interface IGameRound {
  id: number;
  userChoice?: Choice | null;
  correct?: boolean | null;
  gameSessionId?: Pick<IGameSession, 'id'> | null;
  rightIds?: Pick<IMovie, 'id'>[] | null;
  leftIds?: Pick<IMovie, 'id'>[] | null;
}

export type NewGameRound = Omit<IGameRound, 'id'> & { id: null };
