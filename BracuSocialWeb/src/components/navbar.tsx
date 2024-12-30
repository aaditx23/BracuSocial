import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";

import {
  NavigationMenu,
  NavigationMenuItem,
  navigationMenuTriggerStyle,
  NavigationMenuList,
} from "@/components/ui/navigation-menu";
import { Button } from "@/components/ui/button";

const NavBar: React.FC = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const checkLoginStatus = () => {
    const email = localStorage.getItem("email");
    const id = localStorage.getItem("id");
    setIsLoggedIn(email !== "" && id !== "");
  };

  useEffect(() => {
    checkLoginStatus();

    const handleStorageChange = () => {
      checkLoginStatus();
    };

    window.addEventListener("loginStatusChange", handleStorageChange);

    return () => {
      window.removeEventListener("loginStatusChange", handleStorageChange);
    };
  }, []);

  const handleLogout = () => {
    localStorage.setItem("email", "");
    localStorage.setItem("id", "");
    setIsLoggedIn(false);
    window.dispatchEvent(new Event("loginStatusChange"));
    navigate("/");
  };

  const getButtonClass = (path: string) => {
    return location.pathname === path ? "text-black" : "text-gray-500";
  };

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container flex h-14 items-center">
        <NavigationMenu>
          <NavigationMenuList>
            <NavigationMenuItem>
              <Link to="/" className={navigationMenuTriggerStyle()}>
                BRACU Social
              </Link>
            </NavigationMenuItem>
          </NavigationMenuList>
        </NavigationMenu>
        <div className="flex flex-1 items-center justify-end space-x-4">
          <nav className="flex items-center space-x-2">
            <Link to="/">
              <Button variant="ghost" className={getButtonClass("/")}>
                Courses
              </Button>
            </Link>
            <Link to="/preprereg">
              <Button variant="ghost" className={getButtonClass("/preprereg")}>
                PrePreReg
              </Button>
            </Link>
            <Link to="/findroom">
              <Button variant="ghost" className={getButtonClass("/findroom")}>
                Free Rooms
              </Button>
            </Link>
            <Link to="/findlab">
              <Button variant="ghost" className={getButtonClass("/findlab")}>
                Free Labs
              </Button>
            </Link>
            {isLoggedIn && (
              <Link to="/profile">
                <Button variant="ghost" className={getButtonClass("/profile")}>
                  Profile
                </Button>
              </Link>
            )}
            {isLoggedIn && (
              <Link to="/routine">
                <Button variant="ghost" className={getButtonClass("/routine")}>
                  Routine
                </Button>
              </Link>
            )}
            {isLoggedIn && (
              <Link to="/friendroutine">
                <Button
                  variant="ghost"
                  className={getButtonClass("/friendroutine")}
                >
                  Friends' Routine
                </Button>
              </Link>
            )}
            {isLoggedIn && (
              <Link to="/friends">
                <Button variant="ghost" className={getButtonClass("/friends")}>
                  Friends
                </Button>
              </Link>
            )}
            {isLoggedIn ? (
              <Button variant="ghost" onClick={handleLogout}>
                Logout
              </Button>
            ) : (
              <Link to="/auth">
                <Button variant="ghost" className={getButtonClass("/auth")}>
                  Login
                </Button>
              </Link>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
};

export default NavBar;
