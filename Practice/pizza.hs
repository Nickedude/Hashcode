import Data.Char
import Data.Maybe
import Data.List

data Cell = Tomato | Mushroom
  deriving(Show, Eq)

data Pizza = Pizza [[Cell]] R C Min Max 
  deriving(Show)

data Direction = L | R | U | D

type Max = Int
type Min = Int
type R = Int
type C = Int

data Slices = Slices [Slice]
  deriving(Show,Eq)
type Slice = ((R,C),(R,C))

--Reads a file and gives a pizzaz
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

--Founds the cell which is least occuring
leastAmount :: Pizza -> Cell
leastAmount (Pizza cells _ _ _ _) | leastAmountHelp cells < 0 = Tomato
                                  | otherwise = Mushroom
  where
    leastAmountHelp :: [[Cell]] -> Int
    leastAmountHelp cells = foldl (+) 0 (map mapCell (concat cells))

    mapCell :: Cell -> Int
    mapCell Tomato = 1
    mapCell Mushroom = (-1)

--Generates a "map" of which cells are used in a slice
generateTaken :: [[Cell]] -> [[Maybe Cell]]
generateTaken [] = []
generateTaken (c:cs) = ((generateTakenHelp c):(generateTaken cs))
  where
    generateTakenHelp :: [Cell] -> [Maybe Cell]
    generateTakenHelp [] = []
    generateTakenHelp (c:cs) = [Just c]++(generateTakenHelp cs)



--Finds possible slices
possibleSlices :: Pizza -> Slices 
possibleSlices pizza@(Pizza cells rows columns mi ma) = (Slices (minSlices cells 0))
  where
    least = leastAmount pizza       --Get the cell which is least occuring in this pizza
    occupied = generateTaken cells  --Generate a map to keep track of cells already used in a slice

    --Gives minimal slices of the pizza according to the arguments
    minSlices :: [[Cell]] -> R -> [Slice]
    minSlices [] _ = []
    minSlices (c:cs) row = (prospectCell c row 0)++(minSlices cs (row+1)) --Search for prospects on this row and then continue recursively

    --Given a row of the pizza this finds prospects of where to start a slice
    prospectCell :: [Cell] -> R -> C -> [Slice]
    prospectCell [] _ _ = []
    prospectCell (c:cs) row col | c == least = case  formMinSlice cells occupied row  col of          --Try to start forming a minimal slice here if c is of the "right type"
                                               Just slice -> (slice:(prospectCell cs row (col+1)))    --It could be done
                                               Nothing -> prospectCell cs row (col+1)                 --It couldn't be done
                                | otherwise = prospectCell cs row (col+1) 

    --If a prospect has been found we can try to form a slice
    formMinSlice :: [[Cell]] -> [[Maybe Cell]] -> Int -> Int -> Maybe Slice
    formMinSlice  cells taken row col | isTaken taken row col = Nothing                --If this cell is already taken no slice can be created
                                      | otherwise = expandMin ((row,col),(row,col))    --We can try to make a slice here. Create a 1x1 slice and try to expand it.
      where
        --Takes a 1x1 slice and gives a minimal slice (according to mi)
        expandMin :: Slice -> Maybe Slice
        expandMin slice@((lr,lc),(rr,rc)) | (validSlice newslice cells mi ma) = Just newslice
                                          | otherwise = Nothing
          where
            newslice = (expandMinHelp slice)

            expandMinHelp :: Slice -> Slice
            expandMinHelp slice  | validSlice left cells mi ma = left
                                 | validSlice right cells mi ma = right
                                 -- | isValid(leftup) = leftup
                                 -- | isValid(leftdown) = leftdown
                                 -- | isValid(rightup) = rightup
                                 -- | isValid(rightdown) = rightdown
                                 | otherwise = slice --Unexpandable
              where
                left = expandDumb slice L
                right = expandDumb slice R
                leftup = expandDumb left U 
                rightup = expandDumb right U
                leftdown = expandDumb left D
                rightdown = expandDumb right D

            expandDumb :: Slice -> Direction -> Slice
            expandDumb slice@((rr,rc),(lr,lc)) L | isTaken occupied lr (lc-1) = slice
                                                 | sliceSize ((lr,lc-1),(rr,rc)) <= ma && ((lc-1) >= 0) = ((lr,lc-1),(rr,rc))
                                                 | otherwise = slice
            expandDumb slice@((rr,rc),(lr,lc)) R | isTaken occupied rr (rc+1) = slice
                                                 | sliceSize ((lr,lc),(rr,rc+1)) <= ma && ((rc+1) < length(cells !! 0)) = ((lr,lc),(rr,rc+1))
                                                 | otherwise = slice
           -- expandDumb slice@((rr,rc),(lr,lc)) U | sliceSize ((lr,lc+1),(rr,rc)) > ma = undefined
           --                                      | otherwise = undefined
           -- expandDumb slice@((rr,rc),(lr,lc)) D | sliceSize ((lr,lc+1),(rr,rc)) > ma = undefined
           --                                      | otherwise = undefined

--Checks if a slice is valid
--I.E that there is a minimum of each ingredient and that it's not too large
validSlice :: Slice -> [[Cell]] -> Min -> Max -> Bool
validSlice slice cells mi ma = (checkCell slice Mushroom cells) && (checkCell slice Tomato cells) && (checkSize slice)
  where
    --Checks that the slice isn't too large
    checkSize :: Slice -> Bool
    checkSize slice = sliceSize slice < ma

    --Checks how many Tomatos or Mushrooms this slice contains
    checkCell :: Slice -> Cell -> [[Cell]]-> Bool
    checkCell ((lr,lc),(rr,rc)) cell cells | (checkCellHelp 0 lr lc >= mi) = True
                                           | otherwise = False
      where
        --Helper function looping through the slice
        checkCellHelp :: Int -> Int -> Int -> Int
        checkCellHelp nr r c | r == rr && c == rc = nr
                             | r == rr && ((cells !! r) !! c) == cell = checkCellHelp (nr+1) r (c+1)
                             | c == rc && ((cells !! r) !! c) == cell = checkCellHelp (nr+1) (r+1) 0
                             | ((cells !! r) !! c) == cell = checkCellHelp (nr+1) r (c+1)
                             | c == rc = checkCellHelp nr (r+1) 0
                             | r == rr = checkCellHelp nr r (c+1)
                             | otherwise = checkCellHelp nr r (c+1)

--Gives the size of a slice
sliceSize :: Slice -> Int
sliceSize ((rr,rc),(lr,lc)) = (abs(rr-lr)+1)*(abs(rc-lc)+1)

--Checks the taken look up table and returns true of that index is taken 
isTaken :: [[Maybe Cell]] -> Int -> Int -> Bool
isTaken xs row col | (row >= 0) && (row < (length xs) -1) && (col >= 0) && (col <= (length (xs !! 0))) = --Checks that the coordinate is within bounds
                      case (xs !! row) !! col of
                      Nothing -> True
                      Just _ -> False
                   | otherwise = True --Illegal position




--main :: FilePath ->  IO
--main path = do
  --  let {pizza = readPizzaFile path;
      --   }
    --return


 