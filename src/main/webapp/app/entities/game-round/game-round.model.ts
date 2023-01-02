import { IGameSession } from 'app/entities/game-session/game-session.model';
import { IMovie } from 'app/entities/movie/movie.model';
import { Choice } from 'app/entities/enumerations/choice.model';

export interface IGameRound {
  id: number;
  userChoice?: Choice | null;
  correct?: boolean | null;
  gameSessionId?: Pick<IGameSession, 'id'> | null;
  left?: Pick<IMovie, 'id'> | null;
  right?: Pick<IMovie, 'id'> | null;
}

export type NewGameRound = Omit<IGameRound, 'id'> & { id: null };
