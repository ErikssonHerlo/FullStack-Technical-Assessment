import Breadcrumb from '../components/Breadcrumbs/Breadcrumb';
import DefaultLayout from '../layout/DefaultLayout';
import NotFoundImage from '../images/cover/cover-01.png'; 
import { Link } from 'react-router-dom';
import React from 'react';

const NotFound: React.FC = () => {
  return (
    <DefaultLayout>
      <Breadcrumb pageName="404 - Page Not Found" />

      <div className="flex flex-col items-center justify-center gap-8 py-20 text-center">
        <div className="w-full max-w-[400px]">
          <img
            src={NotFoundImage}
            alt="Page Not Found"
            className="w-full object-cover"
          />
        </div>

        <h2 className="text-3xl font-bold text-black dark:text-white">
          Oops! Page not found.
        </h2>
        <p className="max-w-md text-lg text-body-color dark:text-body-color-dark">
          The page you are looking for doesn’t exist or has been moved. Please check the URL or return to the dashboard.
        </p>

        <Link
          to="/dashboard"
          className="rounded-md bg-primary px-6 py-3 text-white transition hover:bg-opacity-90"
        >
          Go back to Dashboard
        </Link>
      </div>
    </DefaultLayout>
  );
};

export default NotFound;
