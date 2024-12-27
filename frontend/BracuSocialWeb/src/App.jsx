import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from "./components/navbar";
import AllCourses from './pages/AllCourses';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container } from 'react-bootstrap';


function App() {
  return (
    <BrowserRouter>
      <Navbar />
        <Routes>
          <Route path="/" element={
            <Container fluid>
              <AllCourses />
            </Container>
          } />
        </Routes>

    </BrowserRouter>
  );
}

export default App;
