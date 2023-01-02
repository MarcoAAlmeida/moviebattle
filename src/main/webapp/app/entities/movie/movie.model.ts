export interface IMovie {
  id: number;
  imdbId?: string | null;
  title?: string | null;
  imdbRating?: number | null;
  imdbVotes?: number | null;
  score?: number | null;
}

export type NewMovie = Omit<IMovie, 'id'> & { id: null };
