import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import LoginLayout from '../../layout/LoginLayout';
import Logo from '../../images/logo/Leverest-Logo-White.svg';
import LogoDark from '../../images/logo/Leverest-Logo.svg';
import Library from '../../images/ilustrations/kanban-logo.png';
import LoginDarkModeSwitcher from '../../components/Header/DarkModeSwitcher';
import { ToastContainer, toast } from 'react-toastify';
import { registerRequest, fetchUserInfo } from '../../services/AuthService';
import 'react-toastify/dist/ReactToastify.css';

const SignUp: React.FC = () => {
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      const registerResponse = await registerRequest({
        email,
        firstName,
        lastName,
        password,
      });

      const token = registerResponse.token; 

      sessionStorage.setItem('authToken', token);

      // Call fetchUserInfo to get user details
      const userInfoResponse = await fetchUserInfo(token);
      const userInfo = userInfoResponse.data;

      localStorage.setItem('UserInfo', JSON.stringify(userInfo));

      // This endpoint just create member user, so we can assume the role is MEMBER and
      // navigate to the member dashboard directly
      navigate('/member-dashboard');
    } catch (error: any) {
      console.error('Registration error:', error);
      toast.error(
        error.message || 'Failed to register. Please try again.',
        {
          position: toast.POSITION.BOTTOM_RIGHT,
        }
      );
    }
  };

  return (
    <LoginLayout>
      <div className="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="w-full flex justify-end pr-6 pt-6">
          <LoginDarkModeSwitcher />
        </div>
        <div className="flex flex-wrap items-center">
          <div className="hidden w-full xl:block xl:w-1/2">
            <div className="py-17.5 px-26 text-center">
              <Link className="mb-5.5 inline-block" to="/">
                <img className="w-96 hidden dark:block" src={Logo} alt="Logo" />
                <img className="w-96 dark:hidden" src={LogoDark} alt="Logo" />
              </Link>
              <p className="2xl:px-20">
                Empower your productivity. Organize, prioritize, and stay on top of your tasks with the Task Management App.
              </p>
              <span className="mt-15 inline-block">
                <img className="w-72" src={Library} alt="Illustration" />
              </span>
            </div>
          </div>

          <div className="w-full border-stroke dark:border-strokedark xl:w-1/2 xl:border-l-2">
            <div className="w-full p-4 sm:p-12.5 xl:p-17.5">
              <span className="mb-1.5 block font-medium">Create your account</span>
              <h2 className="mb-9 text-2xl font-bold text-black dark:text-white sm:text-title-xl2">
                Task Management App
              </h2>
              <form onSubmit={handleSubmit}>
                <div className="mb-4">
                  <label className="mb-2.5 block font-medium text-black dark:text-white">
                    First Name
                  </label>
                  <input
                    type="text"
                    placeholder="Enter your first name"
                    className="w-full rounded-lg border border-stroke bg-transparent py-4 pl-6 pr-10 text-black outline-none focus:border-primary focus-visible:shadow-none dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="mb-2.5 block font-medium text-black dark:text-white">
                    Last Name
                  </label>
                  <input
                    type="text"
                    placeholder="Enter your last name"
                    className="w-full rounded-lg border border-stroke bg-transparent py-4 pl-6 pr-10 text-black outline-none focus:border-primary focus-visible:shadow-none dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="mb-2.5 block font-medium text-black dark:text-white">
                    Email
                  </label>
                  <input
                    type="email"
                    placeholder="Enter your email"
                    className="w-full rounded-lg border border-stroke bg-transparent py-4 pl-6 pr-10 text-black outline-none focus:border-primary focus-visible:shadow-none dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>

                <div className="mb-6">
                  <label className="mb-2.5 block font-medium text-black dark:text-white">
                    Password
                  </label>
                  <input
                    type="password"
                    placeholder="Enter your password"
                    className="w-full rounded-lg border border-stroke bg-transparent py-4 pl-6 pr-10 text-black outline-none focus:border-primary focus-visible:shadow-none dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>

                <div className="mb-5">
                  <input
                    type="submit"
                    value="Sign Up"
                    className="w-full cursor-pointer rounded-lg border border-primary bg-primary p-4 text-white transition hover:bg-opacity-90"
                  />
                </div>

                <div className="mt-6 text-center">
                  <p>
                    Already have an account?{' '}
                    <Link to="/auth/signin" className="text-primary">
                      Sign in
                    </Link>
                  </p>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>

      <ToastContainer />
    </LoginLayout>
  );
};

export default SignUp;
