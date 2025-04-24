import { useState, useEffect } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { BoardColumn, BoardCard, BoardState, CardFormData } from '../types/boardTypes';

const useBoard = () => {
  const [state, setState] = useState<BoardState>({
    columns: [],
    isLoading: false,
    error: null,
  });

  const [selectedCard, setSelectedCard] = useState<BoardCard | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const initialColumns: BoardColumn[] = [
    {
      id: 'backlog',
      title: 'Backlog',
      status: 'backlog',
      cards: [
        {
          id: uuidv4(),
          title: 'Implement login page',
          description: 'Create login form with validation',
          status: 'backlog',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          id: uuidv4(),
          title: 'Design database schema',
          description: 'Plan the initial database structure',
          status: 'backlog',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ],
    },
    {
      id: 'doing',
      title: 'Doing',
      status: 'doing',
      cards: [
        {
          id: uuidv4(),
          title: 'Create board component',
          description: 'Implement drag and drop functionality',
          status: 'doing',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ],
    },
    {
      id: 'review',
      title: 'Review',
      status: 'review',
      cards: [
        {
          id: uuidv4(),
          title: 'User profile page',
          description: 'Needs design review before deployment',
          status: 'review',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ],
    },
    {
      id: 'done',
      title: 'Done',
      status: 'done',
      cards: [
        {
          id: uuidv4(),
          title: 'Setup project',
          description: 'Initialize React + TypeScript project',
          status: 'done',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ],
    },
  ];

  // Cargar datos iniciales
  useEffect(() => {
    setState({
      columns: initialColumns,
      isLoading: false,
      error: null,
    });
  }, []);

  // Función auxiliar para actualizar columnas
  const updateColumns = (callback: (columns: BoardColumn[]) => BoardColumn[]) => {
    setState((prevState) => ({
      ...prevState,
      columns: callback(prevState.columns),
    }));
  };

  const handleCardClick = (cardId: string) => {
    const card = state.columns
      .flatMap((column) => column.cards)
      .find((c) => c.id === cardId);
    if (card) {
      setSelectedCard(card);
      setIsModalOpen(true);
    }
  };

  const handleOpenNewCardModal = () => {
    setSelectedCard(null);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedCard(null);
  };

  const handleSubmitCard = (data: CardFormData) => {
    const now = new Date();

    if (selectedCard) {
      // Actualizar tarjeta existente
      updateColumns((columns) =>
        columns.map((column) => ({
          ...column,
          cards: column.cards.map((card) =>
            card.id === selectedCard.id
              ? { ...card, ...data, updatedAt: now }
              : card
          ),
        }))
      );
    } else {
      // Crear nueva tarjeta
      const newCard: BoardCard = {
        id: uuidv4(),
        ...data,
        createdAt: now,
        updatedAt: now,
      };

      updateColumns((columns) =>
        columns.map((column) =>
          column.status === data.status
            ? { ...column, cards: [...column.cards, newCard] }
            : column
        )
      );
    }

    handleCloseModal();
  };

  const handleDeleteCard = (cardId: string) => {
    updateColumns((columns) =>
      columns.map((column) => ({
        ...column,
        cards: column.cards.filter((card) => card.id !== cardId),
      }))
    );
    handleCloseModal();
  };

  const handleDragEnd = (event: any) => {
    const { active, over } = event;

    if (!over || active.id === over.id) return;

    const cardId = active.id;
    const targetId = over.id;

    let movedCard: BoardCard | null = null;

    // Remover la tarjeta de su columna original
    const columnsWithoutCard = state.columns.map((column) => {
      const cardIndex = column.cards.findIndex((card) => card.id === cardId);
      if (cardIndex !== -1) {
        movedCard = column.cards[cardIndex];
        return {
          ...column,
          cards: column.cards.filter((card) => card.id !== cardId),
        };
      }
      return column;
    });

    if (!movedCard) return;

    // Determinar la columna de destino
    const updatedColumns = columnsWithoutCard.map((column) => {
      if (column.cards.some((card) => card.id === targetId) || column.id === over.id) {
        const targetIndex = column.cards.findIndex((card) => card.id === targetId);

        const newCards = [...column.cards];

        // Si no hay targetId (se suelta al final de la columna), agregar al final
        if (targetIndex === -1) {
          newCards.push(movedCard);
        } else {
          // Insertar la tarjeta en la posición correcta
          newCards.splice(targetIndex, 0, movedCard);
        }

        return { ...column, cards: newCards };
      }
      return column;
    });

    setState({ ...state, columns: updatedColumns });
  };

  return {
    ...state,
    selectedCard,
    isModalOpen,
    handleCardClick,
    handleOpenNewCardModal,
    handleCloseModal,
    handleSubmitCard,
    handleDeleteCard,
    handleDragEnd,
  };
};

export default useBoard;