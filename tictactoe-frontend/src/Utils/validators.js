
/*
validates user input for player name
*/
export const validatePlayerName = (name) => {
    const trimmed = name.trim();
  
    if (!trimmed) return { valid: false, message: "Name cannot be empty." };
  
    const validNameRegex = /^[\w\s]{3,20}$/;
    if (!validNameRegex.test(trimmed)) {
      return {
        valid: false,
        message:
          "Name must be 3-20 characters and contain only letters, numbers, spaces, or underscores.",
      };
    }
  
    return { valid: true, name: trimmed };
  };