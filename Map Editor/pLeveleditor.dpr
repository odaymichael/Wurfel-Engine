program pLeveleditor;

uses
  Forms,
  mLeveleditor in 'mLeveleditor.pas' {Mapeditor},
  Block in 'Block.pas',
  mWelcome in 'mWelcome.pas' {Welcome},
  mMapinfo in 'mMapinfo.pas' {Mapinfo},
  mCreateMap in 'mCreateMap.pas' {CreateMap};

{$R *.res}

begin
  Application.Initialize;
  Application.Title := 'Out There Mapeditor';
  Application.ShowHint := True;
  Application.HintPause := 0;
  Application.CreateForm(TWelcome, Welcome);
  Application.CreateForm(TCreateMap, CreateMap);
  Application.Run;
end.
