import Data.Char
import Data.Text.IO

data Cell = Tomato | Mushroom

data Pizza = Pizza [[Cell]] R C Min Max 

type Max = Int
type Min = Int
type R = Int
type C = Int

data Slices = Slices [((R,C),(R,C))]

test = readFile "pizza.txt"
{-
pizzaParser :: FilePath -> IO Pizza
pizzaParser file = 
	do 
	  text <- (readFile file :: String)
	  (row : _ : col: _ : mi : _ : ma : _ : contents) <- (text :: String)
	  mi' <- digitToInt mi
   	ma' <- digitToInt ma
    let rows = digitToInt row
    let col' = digitToInt col
    let cells =  pizzaParser' contents [[]]
	return (Pizza cells row' col' mi' ma')
	  whereo
    	pizzaParser' :: String -> [[Cell]] -> [[Cell]]
    	pizzaParser' ('\n':xs) ys = pizzaParser' xs (ys:[])
-}


readPizzaFile :: FilePath -> IO (String, String)
readPizzaFile path =
  do  (header : content) <- splitOn "\n" $ readFile path
      (row, col, min', max') <- parseHeader parseHeader
      cells <- map (map toCell) content
      return Pizza cells row col min' max'
      where
        parseHeader :: String -> (Int, Int, Int, Int)
        parseHeader ((r : _ : c: _ : mi : _ : ma : xs) = map (\s -> digitToInt s) (r:c:mi:ma)
        toCell 'T' = Tomato
        toCell 'M' = Mushroom

 