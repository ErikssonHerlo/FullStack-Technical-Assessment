
export interface BoardCard {
  id: string;
  title: string;
  description: string;
  status: 'backlog' | 'doing' | 'review' | 'done';
  createdAt: Date;
  updatedAt: Date;
  assignee?: {
    id: string;
    name: string;
  };
}

export interface BoardColumn {
  id: string;
  title: string;
  status: 'backlog' | 'doing' | 'review' | 'done';
  cards: BoardCard[];
}

export interface BoardState {
  columns: BoardColumn[];
  isLoading: boolean;
  error: string | null;
}

export interface CardFormData {
  title: string;
  description: string;
  status: 'backlog' | 'doing' | 'review' | 'done';
}