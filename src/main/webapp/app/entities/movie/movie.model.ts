import { IGameRound } from 'app/entities/game-round/game-round.model';

export interface IMovie {
  id: number;
  imdbId?: string | null;
  title?: string | null;
  imdbRating?: number | null;
  imdbVotes?: number | null;
  score?: number | null;
  rights?: Pick<IGameRound, 'id'>[] | null;
  lefts?: Pick<IGameRound, 'id'>[] | null;
}

export type NewMovie = Omit<IMovie, 'id'> & { id: null };
