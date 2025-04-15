
/*
validates user input for player name
*/
import { Filter } from 'bad-words';

const filter = new Filter();

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

    if (filter.isProfane(trimmed)) {
        return { valid: false, message: "Name contains inappropriate language." };
    }
  
    return { valid: true, name: trimmed };
  };