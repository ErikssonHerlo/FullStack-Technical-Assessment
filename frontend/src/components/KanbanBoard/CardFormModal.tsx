import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {
  Modal,
  Box,
  Typography,
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  FormHelperText,
  DialogActions,
  DialogContent,
  DialogTitle,
} from '@mui/material';
import { BoardCard, CardFormData } from '../../types/boardTypes';

const cardSchema = yup.object().shape({
  title: yup.string().required('Title is required'),
  description: yup.string().required('Description is required'),
  status: yup
    .string()
    .oneOf(['backlog', 'doing', 'review', 'done'])
    .required('Status is required'),
});

interface CardFormModalProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: CardFormData) => void;
  card?: BoardCard | null;
  onDelete?: () => void;
}

const modalStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  boxShadow: 24,
  borderRadius: 1,
};

const CardFormModal = ({ open, onClose, onSubmit, card, onDelete }: CardFormModalProps) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CardFormData>({
    resolver: yupResolver(cardSchema),
    defaultValues: {
      title: '',
      description: '',
      status: 'backlog',
    },
  });

  useEffect(() => {
    reset({
      title: card?.title || '',
      description: card?.description || '',
      status: card?.status || 'backlog',
    });
  }, [card, reset]);

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={modalStyle}>
        <DialogTitle>
          <Typography variant="h6" component="h2">
            {card ? 'Edit Card' : 'Create New Card'}
          </Typography>
        </DialogTitle>
        <DialogContent>
          <form onSubmit={handleSubmit(onSubmit)}>
            <TextField
              fullWidth
              margin="normal"
              label="Title"
              {...register('title')}
              error={!!errors.title}
              helperText={errors.title?.message}
            />
            <TextField
              fullWidth
              margin="normal"
              label="Description"
              multiline
              rows={4}
              {...register('description')}
              error={!!errors.description}
              helperText={errors.description?.message}
            />
            <FormControl fullWidth margin="normal" error={!!errors.status}>
              <InputLabel>Status</InputLabel>
              <Select
                label="Status"
                {...register('status')}
                defaultValue={card?.status || 'backlog'}
              >
                <MenuItem value="backlog">Backlog</MenuItem>
                <MenuItem value="doing">Doing</MenuItem>
                <MenuItem value="review">Review</MenuItem>
                <MenuItem value="done">Done</MenuItem>
              </Select>
              {errors.status && (
                <FormHelperText>{errors.status.message}</FormHelperText>
              )}
            </FormControl>
          </form>
        </DialogContent>
        <DialogActions>
          {onDelete && (
            <Button color="error" onClick={onDelete}>
              Delete
            </Button>
          )}
          <Box sx={{ flexGrow: 1 }} />
          <Button onClick={onClose}>Cancel</Button>
          <Button onClick={handleSubmit(onSubmit)} color="primary">
            {card ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Box>
    </Modal>
  );
};

export default CardFormModal;