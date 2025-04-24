import { DndContext, closestCenter } from '@dnd-kit/core';
import { Box, Button, CircularProgress, Typography } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import Column from '../components/KanbanBoard/Column';
import useBoard from '../hooks/useBoard';
import CardFormModal from '../components/KanbanBoard/CardFormModal';

const Board = () => {
  const {
    columns,
    isLoading,
    error,
    selectedCard,
    isModalOpen,
    handleCardClick,
    handleOpenNewCardModal,
    handleCloseModal,
    handleSubmitCard,
    handleDeleteCard,
    handleDragEnd,
  } = useBoard();

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" mt={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" mt={4}>
        <Typography color="error">{error}</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      {/* Header */}
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 3,
        }}
      >
        <Typography variant="h4" component="h1">
          Kanban Board
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleOpenNewCardModal}
        >
          Add Card
        </Button>
      </Box>

      {/* Drag-and-Drop Context */}
      <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
        <Box
          sx={{
            display: 'flex',
            overflowX: 'auto',
            gap: 2,
            p: 1,
          }}
        >
          {columns.map((column) => (
            <Column
              key={column.id}
              column={column}
              onCardClick={handleCardClick}
            />
          ))}
        </Box>
      </DndContext>

      {/* Card Form Modal */}
      <CardFormModal
        open={isModalOpen}
        onClose={handleCloseModal}
        onSubmit={handleSubmitCard}
        card={selectedCard}
        onDelete={selectedCard ? () => handleDeleteCard(selectedCard.id) : undefined}
      />
    </Box>
  );
};

export default Board;