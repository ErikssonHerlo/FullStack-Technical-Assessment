import React from 'react';
import CardDataStats from '../../components/CardDataStats';
import ChartOne from '../../components/Charts/ChartOne';
import ChartThree from '../../components/Charts/ChartThree';
import ChartTwo from '../../components/Charts/ChartTwo';
import ChatCard from '../../components/Chat/ChatCard';
import MapOne from '../../components/Maps/MapOne';
import TableOne from '../../components/Tables/TableOne';
import DefaultLayout from '../../layout/DefaultLayout';

const ManagerDashboard: React.FC = () => {
  return (
    <DefaultLayout>
      <h1 className="text-2xl font-bold">Manager Dashboard</h1>
    </DefaultLayout>
  );
};

export default ManagerDashboard;
