export interface IGameSession {
  id: number;
  userId?: number | null;
}

export type NewGameSession = Omit<IGameSession, 'id'> & { id: null };
