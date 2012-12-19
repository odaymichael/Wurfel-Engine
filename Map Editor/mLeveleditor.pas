unit mLeveleditor;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, Menus, StdCtrls, ExtCtrls, Grids, ComCtrls, JPEG, XPMan, Block, Math,
  Spin, mMapinfo, Buttons ;

const CRLF = #13#10;
type TMapeditor = class(TForm)
    btLoadChunkUp: TButton;
    btLoadChunkRight: TButton;
    btLoadChunkDown: TButton;
    btLoadChunkLeft: TButton;
    leChunkCoordX: TLabeledEdit;
    leChunkCoordY: TLabeledEdit;
    MainMenu: TMainMenu;
    miChunk: TMenuItem;
    miOeffnen: TMenuItem;
    miSpeichern: TMenuItem;
    upEbene: TUpDown;
    laEbene: TLabel;
    miNeu: TMenuItem;
    XPManifest1: TXPManifest;
    imMap: TImage;
    gbBlockinfo: TGroupBox;
    imBlockvorschau: TImage;
    laName: TLabel;
    laObstacle: TLabel;
    imSideview: TImage;
    laTransparent: TLabel;
    sbSelect: TSpeedButton;
    sbFill: TSpeedButton;
    sbBucket: TSpeedButton;
    sbPen: TSpeedButton;
    gbTool: TGroupBox;
    sbPipette: TSpeedButton;
    gbSelectionInfo: TGroupBox;
    laSelInfName: TLabel;
    laSelInfID: TLabel;
    laSelInfObstacle: TLabel;
    laSelInfTransparent: TLabel;
    laSelCoords: TLabel;
    miMap: TMenuItem;
    miInfo: TMenuItem;
    SaveDialog: TSaveDialog;
    seHex: TSpinEdit;
    edID: TEdit;
    OpenDialog: TOpenDialog;
    laSelValue: TLabel;
    liBlockList: TListBox;
    OpenChunk: TOpenDialog;
    imMapBottom: TImage;
    cbTwoLayers: TCheckBox;
    procedure upEbeneClick(Sender: TObject; Button: TUDBtnType);
    procedure btLoadChunkUpClick(Sender: TObject);
    procedure btLoadChunkDownClick(Sender: TObject);
    procedure btLoadChunkLeftClick(Sender: TObject);
    procedure btLoadChunkRightClick(Sender: TObject);
    procedure miSpeichernClick(Sender: TObject);
    procedure miInfoClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure edIDChange(Sender: TObject);
    procedure liBlockListClick(Sender: TObject);
    procedure miNeuClick(Sender: TObject);
    procedure sbBucketClick(Sender: TObject);
    procedure imMapMouseUp(Sender: TObject; Button: TMouseButton;
      Shift: TShiftState; X, Y: Integer);
    procedure imMapMouseMove(Sender: TObject; Shift: TShiftState; X,
      Y: Integer);
    procedure imMapMouseDown(Sender: TObject; Button: TMouseButton;
      Shift: TShiftState; X, Y: Integer);
    procedure cbTwoLayersClick(Sender: TObject);
    procedure FormResize(Sender: TObject);
  private
         tilesizeX,tilesizeY, startHeight, startWidth:integer;
         Chunkdata : array of array of array of TBlock;
         last_X,last_Y,last_Z,lastblock:integer;
         changes, mousedown,resizeokay : boolean;
         bitmap:TBitmap;
         imagelist:array[0..99] of array of TBitmap;
          scaleX, scaleY: extended;
         function BoolToStr(b: Boolean): String;
         function savechunk():boolean;
         function newchunk(PX,PY: integer):boolean;
         function loadchunk(PX,PY:integer):boolean;
         procedure refresh_info(id,Hex:integer);
         procedure navigatechunk(PX:integer;PY:integer);
         procedure draw(startY,canvas:integer);
         procedure refresh_selection(x,y,z:integer);overload;
         procedure refresh_selection();overload;
         procedure drawblock(X,Y,Z:integer; Canvas:TCanvas);
         procedure DrawGridOfBlock(x,y:integer);
         procedure FreeRam();
  public
         ChunkSizeX, ChunkSizeY, ChunkSizeZ: integer; 
         mappath: String;
         path_exe : String;
         mapname: String;
         constructor create(AOwner: TComponent;createmappath: string; name: string); overload;virtual;
  end;

var
  Mapeditor: TMapeditor;
  Mapinfo: TMapinfo;

implementation

uses mWelcome;

{$R *.dfm}

function TMapeditor.BoolToStr(b: Boolean): String;
begin
    if b then result := 'ja' else result := 'nein';
end;

constructor TMapeditor.create(AOwner: TComponent;createmappath: string; name: string);
var Stream: TFilestream;
    Writer: TWriter;
    Reader: TReader;
    i:integer;
    tempBlock:TBlock;
    fileversion,filepath:string;
begin
    inherited Create(AOwner);
    mapname := name;
    
    path_exe := ExtractFilePath(Application.Exename);
    // programm-root: ExtractFilePath(ParamStr(0))
    
    tilesizeX := 80;
    tilesizeY := 40;
    ChunkSizeX := 12;
    ChunkSizeY := 28;
    ChunkSizeZ := 20;
    setLength(Chunkdata,ChunkSizeX,ChunkSizeY,ChunkSizeZ);
    
    changes := false;
    mousedown := false;
    lastblock := 0;
    bitmap := TBitmap.Create;
    bitmap.Transparent := True;
    upEbene.Max := ChunkSizeZ-1; 
    
    DoubleBuffered := True;
    startWidth := ChunkSizeX*tilesizeX + ChunkSizeX div 2;
    //startWidth := 1200;
    scaleX := 1;
    scaleY := 1;
    //imMap.Width := startWidth div 2;
    startHeight := (ChunkSizeY+1)*tilesizeY div 2;
    //startHeight:= 484;
    //imMap.Height := startHeight div 2;

    //scan all blocks
    for i:=0 to 100 do begin
        tempBlock := TBlock.Create(i,0);
        liBlockList.Items.Add(tempBlock.name);
        tempBlock.Free;
        
        setLength(imagelist[i],1);
        imagelist[i][0] := TBitmap.Create();
        imagelist[i][0].Transparent := True;
        
        filepath := path_exe+'./bmp/'+IntToStr(i)+'-0.bmp';
        if fileexists(filepath)
           then imagelist[i][0].LoadFromFile(filepath);

    end;
    
    if createmappath<>'' then begin
       //Neue Map anlegen
       mappath := createmappath+name+'\';
       ShowMessage('Created at:'+mappath);
       
       //.otmi datei auf HD sichern
       try 
           Stream := TFilestream.Create(mappath+'map.otmi',fmCreate);
           Writer := TWriter.Create(Stream,500);
           Writer.WriteString('mapname' + CRLF);
           Writer.WriteString(Welcome.getVersion + CRLF);
           Writer.Free;
           Stream.Free;
           //Chunk anlegen
           newchunk(0,0);
       except
            ShowMessage('There was an error creating the map.')
       end;
    end else begin
        //Map laden
        OpenDialog.InitialDir := ExtractFilePath(ParamStr(0));
        if OpenDialog.Execute then begin
            //OTMI laden
            //Pfad des Ordners der Map bestimmen
            for i := Length(OpenDialog.FileName) downto 1 do
                if OpenDialog.FileName[i] = '\' then begin
                   mappath := Copy(OpenDialog.FileName,1,i);
                   break;
               end; 
            
            Stream := TFilestream.Create(mappath+'map.otmi',fmOpenRead);
            Reader := TReader.Create(Stream,500);
            mapname := Reader.ReadString;
            fileversion := Reader.ReadString;
            setlength(fileversion,Pred(Pred(length(fileversion))));
            if Welcome.getVersion <> fileversion then ShowMessage('The map version is different. There may be some problems.');
            Reader.Destroy;
            Stream.Destroy;
            loadchunk(0,0);
        end
        else Exit;
    end;

    if mappath<>''
       then draw(0,1)
       else Welcome.Show;

    resizeokay := true;
end;

procedure TMapeditor.draw(startY,canvas:integer);
var x,y,z: integer;
   thecanvas: TCanvas;
begin
     thecanvas := imMap.Canvas;
     z := upEbene.Position;
     if (canvas = 1) and (cbtwoLayers.Checked) then draw(startY,0);
     if canvas = 0 then begin
         thecanvas := imMapBottom.Canvas;
         z := upEbene.Position -1;
     end;

     //clear field        
     thecanvas.Pen.Style:= psClear;
     thecanvas.Rectangle(
                         0,
                         startY*tileSizeY + startY mod 2 *tileSizeY div 2,
                         imMap.Width,
                         imMap.Height
                         );
     thecanvas.Pen.Style := psSolid;
     thecanvas.Pen.Width := 2;     
     for y := startY to ChunkSizeY-1 do
         for x :=0 to ChunkSizeX-1 do drawblock(x,y,z,thecanvas);
     for y := startY to ChunkSizeY-1 do
         for x :=0 to ChunkSizeX-1 do DrawGridOfBlock(x,y);
             
end;


procedure TMapeditor.drawblock(X,Y,Z:integer; Canvas:TCanvas);
var filepath :string;
 bitmap,bitmaptodraw : TBitmap;
 rect: TRect;
begin
     //Coordinate allowed?
     if (X < ChunkSizeX) and (Y < ChunkSizeY) then begin
         //if image with this value is not in list
         if Length(imagelist[Chunkdata[x][y][z].id]) < Chunkdata[x][y][z].value then begin
            filepath := path_exe+'./bmp/'+IntToStr(Chunkdata[x][y][z].id)+'-'+IntToStr(Chunkdata[x][y][z].value)+'.bmp';
            if fileexists(filepath) then begin
               //if it exists, add it to the list
               imagelist[Chunkdata[x][y][z].id][Chunkdata[x][y][z].value] := TBitmap.Create();
               imagelist[Chunkdata[x][y][z].id][Chunkdata[x][y][z].value].LoadFromFile(filepath);
               bitmaptodraw := imagelist[Chunkdata[x][y][z].id][Chunkdata[x][y][z].value];
            end else begin
                bitmap := TBitmap.Create();
                bitmap.loadFromFile(path_exe+'./bmp/-1-0.bmp');
                bitmaptodraw := bitmap;
            end;    
         end else bitmaptodraw := imagelist[Chunkdata[x][y][z].id][Chunkdata[x][y][z].value];

         //draw image
         rect.Left := Trunc(x*tilesizeX + (tilesizeX div 2) * (y mod 2));
         rect.Top := Trunc(y*tilesizeY div 2);
         rect.Bottom := rect.Top + tilesizeY*2;
         rect.Right  := rect.Left + tilesizeX;
         Canvas.StretchDraw(rect,bitmaptodraw);
     end;
end;

procedure TMapeditor.DrawGridOfBlock(x,y:integer);
begin
   imMap.Canvas.MoveTo(
                       x*tilesizeX + (tilesizeX div 2) * (y mod 2),
                       y*tilesizeY div 2 + tilesizeY div 2);
   imMap.Canvas.LineTo(
                       x*tilesizeX + tilesizeX div 2 + (tilesizeX div 2) * (y mod 2),
                       y*tilesizeY div 2);
   imMap.Canvas.LineTo(
                       x*tilesizeX + tilesizeX + (tilesizeX div 2) * (y mod 2),
                       y*tilesizeY div 2 + tilesizeY div 2);
   imMap.Canvas.LineTo(
                       x*tilesizeX + tilesizeX div 2 + (tilesizeX div 2) * (y mod 2),
                       y*tilesizeY div 2 + tilesizeY);
   imMap.Canvas.LineTo(
                       x*tilesizeX + (tilesizeX div 2) * (y mod 2),
                       y*tilesizeY div 2 + tilesizeY div 2);
end;

procedure TMapeditor.upEbeneClick(Sender: TObject; Button: TUDBtnType);
begin
   laEbene.Caption := 'Ebene: ' + IntToStr(upEbene.Position);
   draw(0,1);
end;

procedure TMapeditor.btLoadChunkUpClick(Sender: TObject);
begin
    navigatechunk(0,1);
end;

procedure TMapeditor.btLoadChunkDownClick(Sender: TObject);
begin
    navigatechunk(0,-1);
end;

procedure TMapeditor.btLoadChunkLeftClick(Sender: TObject);
begin
    navigatechunk(-1,0);
end;

procedure TMapeditor.btLoadChunkRightClick(Sender: TObject);
begin
    navigatechunk(1,0);
end;

procedure TMapeditor.refresh_selection(x,y,z:integer);
var Block: TBlock;
    i: integer;
    Bitmap: TBitmap;
begin
     last_X := x;
     last_Y := y;
     last_Z := z;
     
     Bitmap := TBitmap.Create;
     for i:=0 to 3 do begin
         Bitmap.LoadFromFile(path_exe+'/bmp/'+IntToStr(Chunkdata[x][y][z+3-i].id)+'-'+IntToStr(Chunkdata[x][y][z+3-i].value)+'.bmp');
         imSideView.Canvas.Brush.Bitmap := Bitmap;
         imSideView.Canvas.Rectangle(0,i*(imSideview.Height div 4),imSideview.Width,(i+1)*(imSideview.Height div 4));
         imSideView.Canvas.Brush.Bitmap := nil;
     end;
     Bitmap.Free;
     Block := Chunkdata[x][y][z];
     laSelInfName.Caption := Block.name;
     laSelInfID.Caption := 'ID: '+IntToStr(Block.ID);
     laSelInfObstacle.Caption := 'Obstc: '+ BoolToStr(Block.obstacle);
     laSelInfTransparent.Caption := 'Trsp: '+ BoolToStr(Block.transparent);
     laSelCoords.Caption := 'X:'+IntToStr(x)+' Y:'+IntToStr(y)+' Z:'+IntToStr(z);
     laSelValue.Caption :=  IntToStr(Block.Value);
end;

procedure TMapeditor.refresh_selection();
begin
   refresh_selection(last_X, last_y ,last_z); 
end;

procedure TMapeditor.refresh_info(id,Hex:integer);
var Block: TBlock;
begin
     Block := TBlock.Create(Id,Hex);
     if FileExists(path_exe+'/bmp/'+IntToStr(Block.id)+'-'+IntToStr(seHex.Value)+'.bmp') then begin
        imBlockvorschau.Picture.LoadFromFile(path_exe+'/bmp/'+IntToStr(Block.id)+'-'+IntToStr(seHex.Value)+'.bmp');
        seHex.Enabled := True;
     end else
           if FileExists(path_exe+'/bmp/'+IntToStr(Block.id)+'-0.bmp') then begin
              imBlockvorschau.Picture.LoadFromFile(path_exe+'/bmp/'+IntToStr(Block.id)+'-0.bmp');
              seHex.Value := 0;
              seHex.Enabled := False;
           end else imBlockvorschau.Picture.LoadFromFile(path_exe+'/bmp/-1-0.bmp');;
     laName.Caption := Block.name;
     edID.Text := IntToStr(id);
     laObstacle.Caption := 'Hindernis: '+ BoolToStr(Block.obstacle);
     laTransparent.Caption := 'Blickdicht: '+ BoolToStr(Block.transparent);
     Block.Free;
end;

procedure TMapeditor.miSpeichernClick(Sender: TObject);
begin
   savechunk();
end;

procedure TMapeditor.navigatechunk(PX:integer;PY:integer);
begin
     if changes then savechunk();
      
    //wenn geladen werden kann, aktualisieren
    if loadchunk(StrToInt(leChunkCoordX.Text)+PX, StrToInt(leChunkCoordY.Text)+PY) then begin
       leChunkCoordX.Text := IntToStr(StrToInt(leChunkCoordX.Text)+PX);
       leChunkCoordY.Text := IntToStr(StrToInt(leChunkCoordY.Text)+PY);
    end else//wenn neu erstellt werden muss
       if newchunk(StrToInt(leChunkCoordX.Text)+PX, StrToInt(leChunkCoordY.Text)+PY) then begin
          leChunkCoordX.Text := IntToStr(StrToInt(leChunkCoordX.Text)+PX);
          leChunkCoordY.Text := IntToStr(StrToInt(leChunkCoordY.Text)+PY);
       end;
    draw(0,1);
end;

function TMapeditor.newchunk(PX,PY: integer):boolean;
var x,y,z: integer;
begin
    try
        //RAM leeren und neu füllen
        for y := 0 to ChunkSizeY-1 do
           for x :=0 to ChunkSizeX-1 do
               for z := 0 to ChunkSizeZ-1 do begin
                   Chunkdata[x][y][z].Free;
                   Chunkdata[x][y][z] := TBlock.Create(0,0);
               end;

        //Erde       
        for y := 0 to ChunkSizeY-1 do
            for x := 0 to ChunkSizeX-1 do
              for z := 0 to (ChunkSizeZ-1) div 2 do
                Chunkdata[x][y][z] := TBlock.Create(2,0);

        //Ebene 0 mit Gras füllen
        for y := 0 to ChunkSizeY-1 do
            for x :=0 to ChunkSizeX-1 do
                Chunkdata[x][y][(ChunkSizeZ-1) div 2] := TBlock.Create(1,0);
        changes := true;
        Result := True;
    except
       showmessage('There was an error creating a new chunk');
       Result := False;
    end;
end;

function TMapeditor.savechunk():boolean;
var myFile : TextFile;
    line   : string;
    y,x,z: integer;
begin
   try
       AssignFile(myFile, mappath + 'chunk' + leChunkCoordX.Text+','+leChunkCoordY.Text+'.otmc');
       ReWrite(myFile);
       
       for z := 0 to ChunksizeZ-1 do begin
           WriteLn(myFile, '//'+IntToStr(z));
           for y := 0 to ChunksizeY-1 do begin
               line := '';
               for x :=0 to ChunksizeX- 1 do
                   line := line + IntToStr(Chunkdata[x][y][z].id)+':'+IntToStr(Chunkdata[x][y][z].value)+' ';
               WriteLn(myFile,line); 
           end;
           WriteLn(myFile,'');
       end;
       CloseFile(myFile);
       changes := False;
       Result := True;
   except
       showMessage('Saving went wrong.');
       Result := False
   end;
end;

function TMapeditor.loadchunk(PX,PY:integer):boolean;
var x,y,z,posend,posdots:integer;
    myFile : TextFile;
    line: String;
begin
   if FileExists(mappath + 'chunk' + IntToStr(PX)+','+IntToStr(PY)+'.otmc') then begin 
        try
           FreeRAM();

           AssignFile(myFile, mappath + 'chunk' + IntToStr(PX)+','+IntToStr(PY)+'.otmc');
           reset(myFile);
           z:=-1;
           repeat
                inc(z);
                ReadLn(myFile, line);
                //optionale Kommentarzeile löschen
                if (line[1]='/') and (line[2]='/') //alternativ copy(save,1,2)='//'
                   then ReadLn(myFile, line);
            
                //Ebene
                y := 0;
                repeat
                   x := 0;
                   posend := 0;
                   repeat
                      posdots := pos(':', line);
                      repeat
                         inc(posend);
                      until ((line[posend]= ' ') or (line[posend] = #13));
                      posend := posend - posdots - 1;
                  
                      Chunkdata[x][y][z] := TBlock.Create(
                                         StrToInt(Copy(line, 1, posdots-1)),
                                         StrToInt(Copy(line, posdots+1, posend))
                                         );
                      inc(x);
                      delete(line, 1, posdots+posend+1)
                   until x >= ChunkSizeX;
                   ReadLn(myFile, line);
                   inc(y);
                until y >= ChunkSizeY;
                delete(line, 1, 2);
           until EOF(myfile);
           CloseFile(myFile);
           Result := True;
           draw(0,1);
        except
              showmessage('Reading the file went wrong.');
              Result := False;
        end;
   end
    else Result := False;
end;

procedure TMapeditor.miInfoClick(Sender: TObject);
begin
  Mapinfo := TMapinfo.Create(Mapeditor);
  Mapinfo.ShowModal;
end;

procedure TMapeditor.edIDChange(Sender: TObject);
begin
   refresh_info(StrToInt(edId.Text),seHex.Value);
end;

procedure TMapeditor.liBlockListClick(Sender: TObject);
begin
   edID.Enabled := (liBlockList.ItemIndex = 0);
   seHex.Text := '0';
   refresh_info(liBlockList.ItemIndex-1,0);
end;

procedure TMapeditor.miNeuClick(Sender: TObject);
begin
  newchunk(StrToInt(leChunkCoordX.Text), StrToInt(leChunkCoordY.Text));
  draw(0,1);
end;


procedure TMapeditor.sbBucketClick(Sender: TObject);
begin
    showmessage('buckit not implemented yet')
end;

procedure TMapeditor.imMapMouseUp(Sender: TObject; Button: TMouseButton;
  Shift: TShiftState; X, Y: Integer);
begin
   mousedown := false;
end;

procedure TMapeditor.imMapMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
var sel_x,sel_y,sel_z:integer;
    rek_block:integer;

    procedure get_clicked_tile();
    begin
      //showMessage(IntToStr(Y));
      sel_y := Y div tilesizeY *2;
      sel_x := X div tilesizeX;
      //showMessage(IntToStr((Y - sel_y*tilesizeY div 2)*2));
      if ((x mod tilesizeX) + (y mod tilesizeY))*4 < tilesizeX + tilesizeY div 2
         then begin
                 dec(sel_Y);
                 dec(sel_X);
              end
         else if (-x mod tilesizeX)*4 + (y mod tilesizeY)*8 + 2*tilesizeX < tilesizeY //oben rechts
              then dec(sel_Y)
              else if (x mod tilesizeX) + (y mod tilesizeY) > 3*tilesizeX div 4 + 6*tilesizeY div 8 //unten rechts
                      then inc(sel_Y)
                      else
                          if (-x mod tilesizeX)*32 + (y mod tilesizeY)*64 - tilesizeY*32 > tilesizeX + tilesizeY //unten links
                             then begin
                                     dec(sel_X);
                                     inc(sel_Y);
                                  end;
      if sel_Y < 0 then sel_Y:=0;
      if sel_X < 0 then sel_X:=0;
      if sel_Y >= ChunkSizeY then sel_Y := ChunkSizeY-1;
      if sel_X >= ChunkSizeX then sel_X := ChunkSizeX-1;
      sel_z := upEbene.Position;
    end;
    
    procedure rekursiv_bucket(x,y:integer);
    begin
         //wenn am Rand angekommen abbrechen
         if (x >= ChunkSizeX) or (y >= ChunkSizeX) or (x < 0) or (y<0) then Exit;

         Chunkdata[x][y][upEbene.Position].Free;
         Chunkdata[x][y][upEbene.Position] := TBlock.Create(StrToInt(edID.Text),seHex.Value);
         drawblock(x,y,upEbene.Position,imMap.Canvas);
         
         //Block unten rechts weiter machen, falls nicht am Rand
         if not((x >= ChunkSizeX) or (y >= ChunkSizeX) or (x < 0) or (y<0))
            and (Chunkdata[x+1][y+1][upEbene.Position].id = rek_block) then
            rekursiv_bucket(x+1,y+1);
    end;
begin
   if mousedown then begin
       if sbSelect.Down then begin
          get_clicked_tile();
          refresh_selection(sel_x,sel_y,upEbene.Position);
       end;

       if sbPen.Down then begin
          get_clicked_tile();
          Chunkdata[sel_x][sel_y][sel_z].Free;
          Chunkdata[sel_x][sel_y][sel_z] := TBlock.Create(StrToInt(edID.Text),seHex.Value);
          changes := true;
          draw(sel_y,1);
          refresh_selection();
       end;

       if sbBucket.Down then begin
          get_clicked_tile();
          rek_block := Chunkdata[sel_x][sel_y][upEbene.Position].id;
          rekursiv_bucket(sel_x,sel_y);
          changes := true;
       end;

       if sbFill.Down then begin
          for sel_y := 0 to ChunkSizeY-1 do
              for sel_x :=0 to ChunkSizeX-1 do begin
                  Chunkdata[sel_x][sel_y][upEbene.Position].Free;
                  Chunkdata[sel_x][sel_y][upEbene.Position] := TBlock.Create(StrToInt(edID.Text),seHex.Value);
              end;
          changes := true;
          draw(0,1);
          refresh_selection();
       end;

       if sbPipette.Down then begin
          get_clicked_tile();
          refresh_info(Chunkdata[sel_x][sel_y][upEbene.Position].ID,Chunkdata[sel_x][sel_y][upEbene.Position].value);
          liBlockList.ItemIndex := Chunkdata[sel_x][sel_y][upEbene.Position].ID+1;
          sbPen.Down := True;
        end;
end;
end;

procedure TMapeditor.imMapMouseDown(Sender: TObject; Button: TMouseButton;
  Shift: TShiftState; X, Y: Integer);
begin
   mousedown := true;
   imMapMouseMove( Sender,Shift,X,y);
end;

procedure TMapeditor.FormClose(Sender: TObject; var Action: TCloseAction);
begin
   resizeokay := False;
     try
       FreeRAM();
     except
        showmessage('There was a problem clearning your RAM. Sorry for rubbish.');
     end;                
   //Application.MainForm.Close;
   inherited destroy;
end;

procedure TMapeditor.FreeRAM();
var x,y,z:integer;
begin
   //Ram leeren
   for y := 0 to ChunkSizeY-1 do
      for x :=0 to ChunkSizeX-1 do
          for z := 0 to ChunkSizeZ-1 do
              Chunkdata[x][y][z].Free;
end;

procedure TMapeditor.cbTwoLayersClick(Sender: TObject);
begin
   if cbTwoLayers.Checked then begin
       imMapBottom.Top := imMap.Top + tilesizeY;
       imMapBottom.Visible := True;
       btLoadChunkDown.Top := imMap.Top + imMap.Height + tilesizeY;
       btLoadChunkLeft.Height := imMap.Height + tilesizeY; 
       btLoadChunkRight.Height := imMap.Height + tilesizeY;
       draw(0,1); 
   end else begin
       btLoadChunkDown.Top := imMap.Top + imMap.Height;
       btLoadChunkLeft.Height := imMap.Height; 
       btLoadChunkRight.Height := imMap.Height;
       imMapBottom.Visible := False;
   end; 
end;

procedure TMapeditor.FormResize(Sender: TObject);
begin
   if resizeokay then begin
       imMap.Picture.Graphic.Width := imMap.Width;
       imMap.Picture.Graphic.Height := imMap.Height;
       if cbTwoLayers.Checked then begin
          imMapBottom.Picture.Graphic.Width := imMap.Width;
          imMapBottom.Picture.Graphic.Height := imMap.Height;
       end;
       scaleX := imMap.Width / startWidth;
       scaleY := imMap.Height / startHeight;
       tilesizeX := Trunc(80*scaleX);
       tilesizeY := Trunc(40*scaleY);
       draw(0,1);
   end;
end;

end.
