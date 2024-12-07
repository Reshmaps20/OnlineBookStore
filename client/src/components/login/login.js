import React, { useState } from "react";
import "./login.css";

const Login = () => {

const [isRegister, setIsRegister] = useState(false);
const [formData, setFormData] = useState({
    username: "",
    firstName: "",
    lastName: "",
    password: "",
    confirmPassword: "",
  });

const handleChange = (e) => {
 setFormData({ ...formData, [e.target.name]: e.target.value });
};

const handleNewUser = (e) => {
    setIsRegister(true);
    formReset();
  };
 const formReset = () => {
  setFormData({ username: "", password: "", firstName: "", lastName: "", confirmPassword: "" });
}

const isAnyFieldEmptyForRegistration = () => {
    return  !formData.username || !formData.password || !formData.firstName || !formData.lastName
}

 return (
    <div className="login-container">
      <div className="login-form">
       <h1 data-testid="login-heading">{isRegister ? "Register" : "Login"}</h1>

         <div className="form-group">
            <input type="text" name="username" placeholder="Username" value={formData.username}
                    onChange={handleChange} />
         </div>
         {isRegister && ( <>
            <div className="form-group">
                <input type="text" name="firstName" placeholder="First Name" value={formData.firstName}
                            onChange={handleChange} />
            </div>
            <div className="form-group">
                <input type="text" name="lastName" placeholder="Last Name" value={formData.lastName}
                           onChange={handleChange} />
            </div>
            </>
         )}
         <div className="form-group">
            <input type="password" name="password" placeholder="Password" value={formData.password}
                    onChange={handleChange} />
         </div>
         {isRegister && ( <>
            <div className="form-group confirm-password-container">
                <input type="password" name="confirmPassword" placeholder="Confirm Password" value={formData.confirmPassword}
                          onChange={handleChange}/>
            </div>
            </>
         )}
        <button className="form-button" data-testid="login-id"
          disabled={isAnyFieldEmptyForRegistration()}>
            {isRegister ? "Register" : "Login"}
        </button>
        <button className={`form-button ${isRegister ? 'already-user' : ''}`} data-testid="register-id"
          onClick={handleNewUser} >
          {isRegister ? "Already a user?" : "New User"}
        </button>
      </div>
    </div>
 );
};


export default Login;