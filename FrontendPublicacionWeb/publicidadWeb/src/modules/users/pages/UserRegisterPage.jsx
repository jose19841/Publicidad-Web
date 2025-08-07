// src/modules/users/pages/UserRegisterPage.jsx
import React from "react";
import useRegisterUser from "../hooks/useRegisterUser";
import UserRegisterForm from "../components/UserRegisterForm";

export default function UserRegisterPage() {
  const formProps = useRegisterUser();

  return (
    <div className="container py-5">
      <UserRegisterForm {...formProps} />
    </div>
  );
}
