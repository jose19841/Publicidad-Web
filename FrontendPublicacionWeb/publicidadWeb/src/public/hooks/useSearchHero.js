// src/hooks/useSearchHero.js
import { useState, useCallback } from "react";

export default function useSearchHero({ onSearch, initialValue = "" } = {}) {
  const [query, setQuery] = useState(initialValue);

  const handleChange = useCallback((e) => {
    setQuery(e.target.value);
  }, []);

  const handleSubmit = useCallback((e) => {
    e.preventDefault();
    const value = query.trim();
    if (!value) return;
    onSearch?.(value);
  }, [onSearch, query]);

  return {
    query,
    handleChange,
    handleSubmit,
  };
}
