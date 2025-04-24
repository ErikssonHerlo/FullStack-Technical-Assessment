import React from 'react';
import { Card as MuiCard, CardContent, Typography, IconButton } from '@mui/material';
import { DragIndicator } from '@mui/icons-material';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { BoardCard } from '../../types/boardTypes';

interface CardProps {
  card: BoardCard;
  onClick: () => void;
}

const truncateText = (text: string, maxLength: number) =>
  text.length > maxLength ? `${text.substring(0, maxLength)}...` : text;

const Card = React.memo(({ card, onClick }: CardProps) => {
  const { attributes, listeners, setNodeRef, transform, transition } = useSortable({
    id: card.id,
  });

  const cardStyle = {
    transform: CSS.Transform.toString(transform),
    transition,
    cursor: 'pointer',
  };

  const iconButtonStyle = {
    position: 'absolute',
    right: 4,
    top: 4,
    cursor: 'grab',
  };

  const cardHoverStyle = {
    mb: 2,
    position: 'relative',
    '&:hover': {
      boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
    },
  };

  return (
    <MuiCard ref={setNodeRef} style={cardStyle} onClick={onClick} sx={cardHoverStyle}>
      <IconButton
        {...attributes}
        {...listeners}
        sx={iconButtonStyle}
        aria-label="Drag card"
      >
        <DragIndicator />
      </IconButton>
      <CardContent>
        <Typography variant="h6" component="h3" gutterBottom>
          {card.title}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
          {truncateText(card.description, 100)}
        </Typography>
        {card.assignee && (
          <Typography variant="caption" color="text.secondary">
            Assignee: {card.assignee.name}
          </Typography>
        )}
      </CardContent>
    </MuiCard>
  );
});

export default Card;