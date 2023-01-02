export interface IGameSession {
  id: number;
  userId?: string | null;
}

export type NewGameSession = Omit<IGameSession, 'id'> & { id: null };
