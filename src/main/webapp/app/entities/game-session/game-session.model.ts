export interface IGameSession {
  id: number;
  userId?: string | null;
  finished?: boolean | null;
}

export type NewGameSession = Omit<IGameSession, 'id'> & { id: null };
