import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ToastProvider, ToastViewport } from "@/components/ui/toast"; 
import Navbar from "./components/navbar";
import AllCourses from './pages/AllCourses';
import PrePreRegPage from './pages/PrePreRegPage';
import FindRoom from './pages/FindRoom';
import { Auth } from './pages/Auth';
import ProfilePage from './pages/ProfilePage';
import { FriendsPage } from './pages/FriendsPage';

function App() {
  return (
    <ToastProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-background antialiased">
          <Navbar />
          <main className="container mx-auto px-4 py-8">
            <Routes>
              <Route path="/" element={<AllCourses />} />
              <Route path="/preprereg" element={<PrePreRegPage />} />
              <Route path="/findroom" element={<FindRoom />} />
              <Route path="/auth" element={<Auth />} />
              <Route path="/profile" element={<ProfilePage />} />
              <Route path="/friends" element={<FriendsPage />} />
            </Routes>
            <ToastViewport /> 
          </main>
        </div>
      </BrowserRouter>
    </ToastProvider>
  );
}

export default App;

