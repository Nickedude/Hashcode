import Data.Char
import Data.List

data Cell = Tomato | Mushroom
  deriving(Show)

data Pizza = Pizza [[Cell]] R C Min Max 
  deriving(Show)

type Max = Int
type Min = Int
type R = Int
type C = Int

data Slices = Slices [((R,C),(R,C))]

readPizzaFile :: FilePath -> IO Pizza
readPizzaFile path =
  do  text <- readFile path
      let {(header : content) = lines text;
           (row : col : min' : max' : []) = parseHeader header;
           cells = map (map toCell) content}
      return (Pizza cells row col min' max')
      where
        parseHeader :: String -> [Int]
        parseHeader (r : _ : c: _ : mi : _ : ma : xs) = map (\s -> digitToInt s) (r:c:mi:ma:[])
        parseHeader s = error(s)
        toCell 'T' = Tomato
        toCell 'M' = Mushroom

leastAmount :: Pizza -> Cell
leastAmount (Pizza cells _ _ _ _) | leastAmountHelp cells >= 1 = Tomato
                                  | otherwise = Mushroom
  where
    leastAmountHelp :: [[Cell]] -> Int
    leastAmountHelp cells = foldl (+) 0 (map mapCell (concat cells))

    mapCell :: Cell -> Int
    mapCell Tomato = 1
    mapCell Mushroom = (-1)





--main :: FilePath ->  IO
--main path = do
  --  let {pizza = readPizzaFile path;
      --   }
    --return


 