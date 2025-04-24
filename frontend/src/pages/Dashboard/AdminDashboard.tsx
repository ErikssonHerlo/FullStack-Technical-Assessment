import React from 'react';
import Board from '../Board';
import DefaultLayout from '../../layout/DefaultLayout';

const AdminDashboard: React.FC = () => {
  return (
    <DefaultLayout>
      <Board>
      </Board>
    </DefaultLayout>
  );
};

export default AdminDashboard;
