import { Box, Typography, Paper } from '@mui/material';
import { useDroppable } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';
import Card from './Card';
import { BoardColumn } from '../../types/boardTypes';

interface ColumnProps {
  column: BoardColumn;
  onCardClick: (cardId: string) => void;
}

const columnStyle = {
  minWidth: 300,
  mx: 1,
};

const paperStyle = {
  p: 2,
  backgroundColor: 'background.paper',
};

const droppableStyle = {
  minHeight: 100,
};

const Column = ({ column, onCardClick }: ColumnProps) => {
  const { setNodeRef } = useDroppable({
    id: column.id,
  });

  const renderCards = () =>
    column.cards.map((card) => (
      <Card key={card.id} card={card} onClick={() => onCardClick(card.id)} />
    ));

  return (
    <Box sx={columnStyle}>
      <Paper elevation={2} sx={paperStyle}>
        <Typography variant="h6" component="h2" align="center" gutterBottom>
          {column.title}
        </Typography>
        <SortableContext
          id={column.id}
          items={column.cards.map((card) => card.id)}
          strategy={verticalListSortingStrategy}
        >
          <Box ref={setNodeRef} sx={droppableStyle}>
            {renderCards()}
          </Box>
        </SortableContext>
      </Paper>
    </Box>
  );
};

export default Column;