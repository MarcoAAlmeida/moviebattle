import { Choice } from 'app/entities/enumerations/choice.model';

export interface IGameRound {
  id: number;
  gameSessionId?: string | null;
  rightImdbId?: string | null;
  leftImdbId?: string | null;
  userChoice?: Choice | null;
  correct?: boolean | null;
}

export type NewGameRound = Omit<IGameRound, 'id'> & { id: null };
